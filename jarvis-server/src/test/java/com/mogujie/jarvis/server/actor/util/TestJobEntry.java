package com.mogujie.jarvis.server.actor.util;

import java.util.List;
import java.util.Map;

import com.mogujie.jarvis.protocol.JobDependencyEntryProtos;
import com.mogujie.jarvis.protocol.JobScheduleExpressionEntryProtos;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/1/18.
 * used by jarvis-parent
 */
public class TestJobEntry {
    private long jobId;
    private String jobName;
    private String jobType;
    private Integer status;
    private String content;
    private Map<String, Object> params;
    private String appName;
    private Integer workerGroupId;
    private Integer priority;
    private Long activeStartTime;
    private Long activeEndTime;
    private Integer expiredTime;
    private Integer failedAttempts;
    private Integer failedInterval;
    private List<JobDependencyEntryProtos.DependencyEntry> dependencyList;
    private List<JobScheduleExpressionEntryProtos.ScheduleExpressionEntry> scheduleExpressionList;

    public TestJobEntry() {
    }

    public TestJobEntry(
           String jobName, String jobType, Integer status,
            String content, Map<String, Object> params, String appName,
            Integer workerGroupId, Integer priority, Long activeStartTime,
            Long activeEndTime, Integer expiredTime, Integer failedAttempts,
            Integer failedInterval, List<JobDependencyEntryProtos.DependencyEntry> dependencyList,
            List<JobScheduleExpressionEntryProtos.ScheduleExpressionEntry> scheduleExpressionList) {

        this.jobName = jobName;
        this.jobType = jobType;
        this.status = status;
        this.content = content;
        this.params = params;
        this.appName = appName;
        this.workerGroupId = workerGroupId;
        this.priority = priority;
        this.activeStartTime = activeStartTime;
        this.activeEndTime = activeEndTime;
        this.expiredTime = expiredTime;
        this.failedAttempts = failedAttempts;
        this.failedInterval = failedInterval;
        this.dependencyList = dependencyList;
        this.scheduleExpressionList = scheduleExpressionList;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getWorkerGroupId() {
        return workerGroupId;
    }

    public void setWorkerGroupId(Integer workerGroupId) {
        this.workerGroupId = workerGroupId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getActiveStartTime() {
        return activeStartTime;
    }

    public void setActiveStartTime(Long activeStartTime) {
        this.activeStartTime = activeStartTime;
    }

    public Long getActiveEndTime() {
        return activeEndTime;
    }

    public void setActiveEndTime(Long activeEndTime) {
        this.activeEndTime = activeEndTime;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Integer getFailedInterval() {
        return failedInterval;
    }

    public void setFailedInterval(Integer failedInterval) {
        this.failedInterval = failedInterval;
    }

    public List<JobDependencyEntryProtos.DependencyEntry> getDependencyList() {
        return dependencyList;
    }

    public void setDependencyList(List<JobDependencyEntryProtos.DependencyEntry> dependencyList) {
        this.dependencyList = dependencyList;
    }

    public List<JobScheduleExpressionEntryProtos.ScheduleExpressionEntry> getScheduleExpressionList() {
        return scheduleExpressionList;
    }

    public void setScheduleExpressionList(List<JobScheduleExpressionEntryProtos.ScheduleExpressionEntry> scheduleExpressionList) {
        this.scheduleExpressionList = scheduleExpressionList;
    }
}
