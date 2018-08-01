package io.choerodon.asgard.saga;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.choerodon.asgard.saga.dto.PollBatchDTO;
import io.choerodon.asgard.saga.dto.PollCodeDTO;
import io.choerodon.asgard.saga.dto.SagaTaskInstanceDTO;
import io.choerodon.asgard.saga.dto.SagaTaskInstanceStatusDTO;
import io.choerodon.core.saga.SagaDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SagaMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaMonitor.class);

    private ChoerodonSagaProperties choerodonSagaProperties;

    private SagaClient sagaClient;

    private Executor executor;

    static final Map<String, SagaTaskInvokeBean> invokeBeanMap = new HashMap<>();

    private DataSourceTransactionManager transactionManager;

    private Environment environment;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private volatile Set<SagaTaskInstanceDTO> msgQueue;

    public SagaMonitor(ChoerodonSagaProperties choerodonSagaProperties,
                       SagaClient sagaClient,
                       Executor executor,
                       DataSourceTransactionManager transactionManager,
                       Environment environment) {
        this.choerodonSagaProperties = choerodonSagaProperties;
        this.sagaClient = sagaClient;
        this.executor = executor;
        this.transactionManager = transactionManager;
        this.environment = environment;
        msgQueue = Collections.synchronizedSet(new HashSet<>(choerodonSagaProperties.getMaxPollSize()));
    }

    @PostConstruct
    private void start() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        List<PollCodeDTO> codeDTOS = invokeBeanMap.entrySet().stream().map(t -> new PollCodeDTO(t.getValue().sagaTask.sagaCode(),
                t.getValue().sagaTask.code())).collect(Collectors.toList());
        final int maxPollSize = choerodonSagaProperties.getMaxPollSize();
        try {
            String instance = InetAddress.getLocalHost().getHostAddress() + ":" + environment.getProperty("server.port");
            LOGGER.info("sagaMonitor prepare to start saga consumer, pollTasks {}, instance {}, maxPollSize {}, ", codeDTOS, instance, maxPollSize);
            scheduledExecutorService.scheduleWithFixedDelay(() -> {
                if (msgQueue.isEmpty()) {
                    try {
                        Set<SagaTaskInstanceDTO> set = sagaClient.pollBatch(new PollBatchDTO(instance, codeDTOS, maxPollSize));
                        LOGGER.debug("sagaMonitor polled messages, size {} data {}", set.size(), set);
                        msgQueue.addAll(set);
                        msgQueue.forEach(t -> executor.execute(new InvokeTask(t)));
                    } catch (Exception e) {
                        LOGGER.warn("sagaMonitor poll error {}", e.getMessage());
                    }
                }
            }, 20, choerodonSagaProperties.getPollInterval(), TimeUnit.SECONDS);
        } catch (UnknownHostException e) {
            LOGGER.error("sagaMonitor can't get localhost, failed to start saga consumer. {}", e.getCause());
        }
    }

    private class InvokeTask implements Runnable {

        private final SagaTaskInstanceDTO dto;

        InvokeTask(SagaTaskInstanceDTO dto) {
            this.dto = dto;
        }

        @Override
        public void run() {
            try {
                invoke(dto);
            } catch (Exception e) {
                LOGGER.warn("sagaMonitor consume message error, cause {}", e.getMessage());
            } finally {
                msgQueue.remove(dto);
            }
        }

        private void invoke(SagaTaskInstanceDTO data) {
            final String key = data.getSagaCode() + data.getTaskCode();
            SagaTaskInvokeBean invokeBean = invokeBeanMap.get(key);
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(invokeBean.sagaTask.transactionDefinition());
            TransactionStatus status = transactionManager.getTransaction(def);
            try {
                invokeBean.method.setAccessible(true);
                final Object result = invokeBean.method.invoke(invokeBean.object, data.getInput());
                SagaTaskInstanceDTO updateResult = sagaClient.updateStatus(data.getId(), new SagaTaskInstanceStatusDTO(data.getId(),
                        SagaDefinition.InstanceStatus.COMPLETED.name(), resultToJson(result), null));
                LOGGER.debug("updateStatus result {} time {}", updateResult, System.currentTimeMillis());
                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);
                String errorMsg = getErrorInfoFromException(e);
                sagaClient.updateStatus(data.getId(), new SagaTaskInstanceStatusDTO(data.getId(),
                        SagaDefinition.InstanceStatus.FAILED.name(), null, errorMsg));
                LOGGER.error("sagaMonitor invoke method error, msg {}, cause {}", data, errorMsg);
            }
        }

        private String resultToJson(final Object result) throws IOException {
            if (result == null) {
                return null;
            }
            if (result instanceof String) {
                String resultStr = (String) result;
                JsonNode jsonNode = objectMapper.readTree(resultStr);
                if (jsonNode instanceof ObjectNode) {
                    return resultStr;
                }
            }
            return objectMapper.writeValueAsString(result);
        }

        private String getErrorInfoFromException(Throwable e) {
            try {
                if (e instanceof InvocationTargetException) {
                    e = ((InvocationTargetException) e).getTargetException();
                }
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                return "\r\n" + sw.toString() + "\r\n";
            } catch (Exception e2) {
                return "bad getErrorInfoFromException";
            }
        }
    }

}
