package com.mogujie.jarvis.rest.vo;

import java.util.List;

import com.mogujie.jarvis.rest.vo.JobDependencyVo.DependencyEntry;
import com.mogujie.jarvis.rest.vo.JobScheduleExpVo.ScheduleExpressionEntry;

/**
 * jobVo类
 *
 * @author muming
 */
public class JobVo extends AbstractVo {
    private Long jobId;
    private String jobName;
    private String jobType;
    private Integer status;
    private Integer contentType;
    private String content;
    private String params;
    private String appName;
    private Integer workerGroupId;
    private Integer departmentId;
    private String bizGroups;
    private Integer priority;
    private boolean isTemp;
    private Long activeStartDate;
    private Long activeEndDate;
    private Integer expiredTime; // 单位秒
    private Integer failedAttempts;
    private Integer failedInterval;
    private List<DependencyEntry> dependencyList;
    private List<ScheduleExpressionEntry> scheduleExpressionList;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
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

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getBizGroups() {
        return bizGroups;
    }

    public JobVo setBizGroups(String bizGroups) {
        this.bizGroups = bizGroups;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean isTemp() {
        return isTemp;
    }

    public void setTemp(boolean isTemp) {
        this.isTemp = isTemp;
    }

    public Long getActiveStartDate() {
        return activeStartDate;
    }

    public void setActiveStartDate(Long activeStartDate) {
        this.activeStartDate = activeStartDate;
    }

    public Long getActiveEndDate() {
        return activeEndDate;
    }

    public void setActiveEndDate(Long activeEndDate) {
        this.activeEndDate = activeEndDate;
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

    public List<DependencyEntry> getDependencyList() {
        return dependencyList;
    }

    public void setDependencyList(List<DependencyEntry> dependencyList) {
        this.dependencyList = dependencyList;
    }

    public List<ScheduleExpressionEntry> getScheduleExpressionList() {
        return scheduleExpressionList;
    }

    public void setScheduleExpressionList(List<ScheduleExpressionEntry> scheduleExpressionList) {
        this.scheduleExpressionList = scheduleExpressionList;
    }

    //---------- 默认值处理 -----------------------
    public String getAppName(String defaultValue) {
        return (appName != null) ? appName : defaultValue;
    }

    public Integer getStatus(Integer defaultValue) {
        return status != null ? status : defaultValue;
    }

    public String getParams(String defaultValue) {
        return params != null ? params : defaultValue;
    }

    public Long getActiveStartDate(Long defaultValue) {
        return (activeStartDate != null) ? activeStartDate : defaultValue;
    }

    public Long getActiveEndDate(Long defaultValue) {
        return (activeEndDate != null) ? activeEndDate : defaultValue;
    }

    public Integer getPriority(Integer defaultValue) {
        return (priority != null) ? priority : defaultValue;
    }

    public Integer getExpiredTime(Integer defaultValue) {
        return (expiredTime != null) ? expiredTime : defaultValue;
    }

    public Integer getFailedAttempts(Integer defaultValue) {
        return (failedAttempts != null) ? failedAttempts : defaultValue;
    }

    public Integer getFailedInterval(Integer defaultValue) {
        return (failedInterval != null) ? failedInterval : defaultValue;
    }

    public Integer getDepartmentId(Integer defaultValue) {
        return (departmentId != null) ? departmentId : defaultValue;
    }

    public String getBizGroups(String defaultValue) {
        return (bizGroups != null) ? bizGroups : defaultValue;
    }

}
