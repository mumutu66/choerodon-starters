package org.gitlab4j.api.webhook;

/*
 *   The MIT License (MIT)
 *   
 *   Copyright (c) 2017 Greg Messner <greg@messners.com>
 *   
 *   Permission is hereby granted, free of charge, to any person obtaining a copy of
 *   this software and associated documentation files (the "Software"), to deal in
 *   the Software without restriction, including without limitation the rights to
 *   use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *   the Software, and to permit persons to whom the Software is furnished to do so,
 *   subject to the following conditions:
 *   
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *   
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *   FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *   COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *   IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitlab4j.api.models.User;

@XmlAccessorType(XmlAccessType.FIELD)
public class BuildEvent implements Event {

    public static final String X_GITLAB_EVENT = "Build Hook";
    public static final String OBJECT_KIND = "build";

    private String ref;
    private Boolean tag;
    private String beforeSha;
    private String sha;
    private Integer buildId;
    private String buildName;
    private String buildStage;
    private String buildStatus;
    private Date buildStarted_at;
    private Date buildFinished_at;
    private Integer buildDuration;
    private Boolean buildAllowFailure;
    private Integer projectId;
    private String projectName;
    private User user;
    private BuildEventCommit commit;
    private EventRepository repository;

    public String getObjectKind() {
        return (OBJECT_KIND);
    }

    public void setObjectKind(String objectKind) {
        if (!OBJECT_KIND.equals(objectKind))
            throw new RuntimeException("Invalid object_kind (" + objectKind + "), must be '" + OBJECT_KIND + "'");
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Boolean getTag() {
        return tag;
    }

    public void setTag(Boolean tag) {
        this.tag = tag;
    }

    public String getBeforeSha() {
        return beforeSha;
    }

    public void setBeforeSha(String beforeSha) {
        this.beforeSha = beforeSha;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getBuildStage() {
        return buildStage;
    }

    public void setBuildStage(String buildStage) {
        this.buildStage = buildStage;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    public Date getBuildStarted_at() {
        return buildStarted_at;
    }

    public void setBuildStarted_at(Date buildStarted_at) {
        this.buildStarted_at = buildStarted_at;
    }

    public Date getBuildFinished_at() {
        return buildFinished_at;
    }

    public void setBuildFinished_at(Date buildFinished_at) {
        this.buildFinished_at = buildFinished_at;
    }

    public Integer getBuildDuration() {
        return buildDuration;
    }

    public void setBuildDuration(Integer buildDuration) {
        this.buildDuration = buildDuration;
    }

    public Boolean getBuildAllowFailure() {
        return buildAllowFailure;
    }

    public void setBuildAllowFailure(Boolean buildAllowFailure) {
        this.buildAllowFailure = buildAllowFailure;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BuildEventCommit getCommit() {
        return commit;
    }

    public void setCommit(BuildEventCommit commit) {
        this.commit = commit;
    }

    public EventRepository getRepository() {
        return repository;
    }

    public void setRepository(EventRepository repository) {
        this.repository = repository;
    }
}
