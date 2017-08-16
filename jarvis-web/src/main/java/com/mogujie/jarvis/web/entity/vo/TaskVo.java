package com.mogujie.jarvis.web.entity.vo;

import com.mogujie.jarvis.dto.generate.Task;

import java.util.Date;
import java.util.List;

/**
 * Created by hejian on 15/9/17.
 */
public class TaskVo extends Task {
    private String jobName;
    private String jobType;
    private String submitUser;
    private Integer priority;
    private String appName;
    private String workerGroupId;
    private String workerGroupName;
    private String ip;

    private Integer isTemp;

    private Long executeTime;
    private Long avgExecuteTime;

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

    public String getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public Long getAvgExecuteTime() {
        return avgExecuteTime;
    }

    public void setAvgExecuteTime(Long avgExecuteTime) {
        this.avgExecuteTime = avgExecuteTime;
    }

    public String getWorkerGroupId() {
        return workerGroupId;
    }

    public void setWorkerGroupId(String workerGroupId) {
        this.workerGroupId = workerGroupId;
    }

    public String getWorkerGroupName() {
        return workerGroupName;
    }

    public void setWorkerGroupName(String workerGroupName) {
        this.workerGroupName = workerGroupName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(Integer isTemp) {
        this.isTemp = isTemp;
    }
}
