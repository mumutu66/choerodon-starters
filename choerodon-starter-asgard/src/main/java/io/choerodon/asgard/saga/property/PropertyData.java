package io.choerodon.asgard.saga.property;

import java.util.ArrayList;
import java.util.List;

public class PropertyData {

    private String service;

    private List<Saga> sagas = new ArrayList<>();

    private List<SagaTask> sagaTasks = new ArrayList<>();

    public List<Saga> getSagas() {
        return sagas;
    }

    public void addSaga(Saga saga) {
        this.sagas.add(saga);
    }

    public List<SagaTask> getSagaTasks() {
        return sagaTasks;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void addSagaTask(SagaTask sagaTask) {
        this.sagaTasks.add(sagaTask);
    }

    public static class Saga {

        private String code;

        private String description;

        private String inputSchema;

        private String inputSchemaSource;

        public Saga(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public Saga() {
        }

        public String getInputSchema() {
            return inputSchema;
        }

        public void setInputSchema(String inputSchema) {
            this.inputSchema = inputSchema;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getInputSchemaSource() {
            return inputSchemaSource;
        }

        public void setInputSchemaSource(String inputSchemaSource) {
            this.inputSchemaSource = inputSchemaSource;
        }
    }

    public static class SagaTask {

        private String code;

        private String description;

        private String sagaCode;

        private Integer seq;

        private Integer maxRetryCount;

        private Integer timeoutSeconds;

        private String timeoutPolicy;

        private Integer concurrentLimitNum;

        private String concurrentLimitPolicy;

        private String outputSchema;

        private String outputSchemaSource;

        public SagaTask() {
        }

        public SagaTask(String code, String description, String sagaCode, Integer seq, Integer maxRetryCount) {
            this.code = code;
            this.description = description;
            this.sagaCode = sagaCode;
            this.seq = seq;
            this.maxRetryCount = maxRetryCount;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSagaCode() {
            return sagaCode;
        }

        public void setSagaCode(String sagaCode) {
            this.sagaCode = sagaCode;
        }

        public Integer getSeq() {
            return seq;
        }

        public void setSeq(Integer seq) {
            this.seq = seq;
        }

        public Integer getMaxRetryCount() {
            return maxRetryCount;
        }

        public void setMaxRetryCount(Integer maxRetryCount) {
            this.maxRetryCount = maxRetryCount;
        }

        public Integer getTimeoutSeconds() {
            return timeoutSeconds;
        }

        public void setTimeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
        }

        public String getTimeoutPolicy() {
            return timeoutPolicy;
        }

        public void setTimeoutPolicy(String timeoutPolicy) {
            this.timeoutPolicy = timeoutPolicy;
        }

        public Integer getConcurrentLimitNum() {
            return concurrentLimitNum;
        }

        public void setConcurrentLimitNum(Integer concurrentLimitNum) {
            this.concurrentLimitNum = concurrentLimitNum;
        }

        public String getConcurrentLimitPolicy() {
            return concurrentLimitPolicy;
        }

        public void setConcurrentLimitPolicy(String concurrentLimitPolicy) {
            this.concurrentLimitPolicy = concurrentLimitPolicy;
        }

        public String getOutputSchema() {
            return outputSchema;
        }

        public void setOutputSchema(String outputSchema) {
            this.outputSchema = outputSchema;
        }

        public String getOutputSchemaSource() {
            return outputSchemaSource;
        }

        public void setOutputSchemaSource(String outputSchemaSource) {
            this.outputSchemaSource = outputSchemaSource;
        }
    }

    @Override
    public String toString() {
        return "PropertyData{" +
                "sagas=" + sagas +
                ", sagaTasks=" + sagaTasks +
                '}';
    }
}
