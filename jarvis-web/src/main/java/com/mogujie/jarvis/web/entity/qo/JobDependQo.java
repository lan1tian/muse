package com.mogujie.jarvis.web.entity.qo;

import java.util.List;

/**
 * @author  muming
 */
public class JobDependQo {

    private long jobId;
    private int showTaskStartTime;
    private int showTaskEndTime;
    private List<Integer> statusList;
    private List<Long> jobIdList;

    public long getJobId() {
        return jobId;
    }

    public JobDependQo setJobId(long jobId) {
        this.jobId = jobId;
        return this;
    }

    public int getShowTaskStartTime() {
        return showTaskStartTime;
    }

    public JobDependQo setShowTaskStartTime(int showTaskStartTime) {
        this.showTaskStartTime = showTaskStartTime;
        return this;
    }

    public int getShowTaskEndTime() {
        return showTaskEndTime;
    }

    public JobDependQo setShowTaskEndTime(int showTaskEndTime) {
        this.showTaskEndTime = showTaskEndTime;
        return this;
    }

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }

    public List<Long> getJobIdList() {
        return jobIdList;
    }

    public void setJobIdList(List<Long> jobIdList) {
        this.jobIdList = jobIdList;
    }
}
