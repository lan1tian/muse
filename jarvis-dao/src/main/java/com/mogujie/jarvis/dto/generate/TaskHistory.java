package com.mogujie.jarvis.dto.generate;

import java.util.Date;

public class TaskHistory extends TaskHistoryKey {
    private Long jobId;

    private String params;

    private Date scheduleTime;

    private Date dataTime;

    private Float progress;

    private Integer type;

    private Integer status;

    private String finishReason;

    private Integer appId;

    private Integer workerId;

    private String executeUser;

    private Date executeStartTime;

    private Date executeEndTime;

    private Date createTime;

    private Date updateTime;

    private Integer alarmEnable;

    private String content;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getExecuteUser() {
        return executeUser;
    }

    public void setExecuteUser(String executeUser) {
        this.executeUser = executeUser;
    }

    public Date getExecuteStartTime() {
        return executeStartTime;
    }

    public void setExecuteStartTime(Date executeStartTime) {
        this.executeStartTime = executeStartTime;
    }

    public Date getExecuteEndTime() {
        return executeEndTime;
    }

    public void setExecuteEndTime(Date executeEndTime) {
        this.executeEndTime = executeEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getAlarmEnable() {
        return alarmEnable;
    }

    public void setAlarmEnable(Integer alarmEnable) {
        this.alarmEnable = alarmEnable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}