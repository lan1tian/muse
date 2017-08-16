/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月15日 下午4:46:47
 */

package com.mogujie.jarvis.core.domain;

import java.util.Map;
import java.util.Objects;

import org.joda.time.DateTime;

public final class TaskDetail {

    private String fullId;
    private String taskName;
    private String appName;
    private String user;
    private String jobType;
    private String content;
    private int priority;
    private int groupId;
    private Map<String, Object> parameters;
    private int expiredTime; //单位秒
    private DateTime scheduleTime;
    private DateTime dataTime;
    private int failedRetries;
    private int failedInterval;
    private boolean isChanged = false;

    private TaskDetail() {
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFullId() {
        return fullId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getAppName() {
        return appName;
    }

    public String getUser() {
        return user;
    }

    public String getJobType() {
        return jobType;
    }

    public String getContent() {
        return content;
    }

    public int getPriority() {
        return priority;
    }

    public int getGroupId() {
        return groupId;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public DateTime getScheduleTime() {
        return scheduleTime;
    }

    public DateTime getDataTime() {
        return dataTime;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public int getFailedRetries() {
        return failedRetries;
    }

    public int getFailedInterval() {
        return failedInterval;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullId, taskName, appName, user, jobType, content, priority, groupId, parameters, expiredTime, scheduleTime, dataTime,
                failedRetries, failedInterval);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof TaskDetail) {
            TaskDetail other = (TaskDetail) obj;
            return Objects.equals(fullId, other.fullId) && Objects.equals(taskName, other.taskName) && Objects.equals(appName, other.appName)
                    && Objects.equals(user, other.user) && Objects.equals(jobType, other.jobType) && Objects.equals(content, other.content)
                    && Objects.equals(priority, other.priority) && Objects.equals(groupId, other.groupId)
                    && Objects.equals(parameters, other.parameters) && Objects.equals(expiredTime, other.expiredTime)
                    && Objects.equals(scheduleTime, other.scheduleTime) && Objects.equals(dataTime, other.dataTime)
                    && Objects.equals(failedRetries, other.failedRetries) && Objects.equals(failedInterval, other.failedInterval);

        }

        return false;
    }

    public static TaskDetailBuilder newTaskDetailBuilder() {
        return new TaskDetailBuilder();
    }

    public static TaskDetailBuilder newTaskDetailBuilder(TaskDetail taskDetail) {
        return new TaskDetailBuilder(taskDetail);
    }

    public static final class TaskDetailBuilder {

        private TaskDetail task = new TaskDetail();;

        private TaskDetailBuilder() {
        }

        public TaskDetailBuilder(TaskDetail taskDetail) {
            this.task = taskDetail;
        }

        public TaskDetailBuilder setFullId(String fullId) {
            this.task.fullId = fullId;
            return this;
        }

        public TaskDetailBuilder setTaskName(String taskName) {
            this.task.taskName = taskName;
            return this;
        }

        public TaskDetailBuilder setAppName(String appName) {
            this.task.appName = appName;
            return this;
        }

        public TaskDetailBuilder setUser(String user) {
            this.task.user = user;
            return this;
        }

        public TaskDetailBuilder setJobType(String jobType) {
            this.task.jobType = jobType;
            return this;
        }

        public TaskDetailBuilder setContent(String content) {
            this.task.content = content;
            return this;
        }

        public TaskDetailBuilder setPriority(int priority) {
            this.task.priority = priority;
            return this;
        }

        public TaskDetailBuilder setGroupId(int groupId) {
            this.task.groupId = groupId;
            return this;
        }

        public TaskDetailBuilder setParameters(Map<String, Object> parameters) {
            this.task.parameters = parameters;
            return this;
        }

        public TaskDetailBuilder setScheduleTime(DateTime scheduleTime) {
            this.task.scheduleTime = scheduleTime;
            return this;
        }

        public TaskDetailBuilder setDataTime(DateTime dataTime) {
            this.task.dataTime = dataTime;
            return this;
        }

        public TaskDetailBuilder setExpiredTime(int expiredTime) {
            this.task.expiredTime = expiredTime;
            return this;
        }

        public TaskDetailBuilder setFailedRetries(int failedRetries) {
            this.task.failedRetries = failedRetries;
            return this;
        }

        public TaskDetailBuilder setFailedInterval(int failedInterval) {
            this.task.failedInterval = failedInterval;
            return this;
        }

        public TaskDetail build() {
            return task;
        }
    }

}
