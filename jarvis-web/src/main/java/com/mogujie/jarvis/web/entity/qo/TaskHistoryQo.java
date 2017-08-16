package com.mogujie.jarvis.web.entity.qo;

/**
 * Created by hejian on 16/1/13.
 */
public class TaskHistoryQo {
    private Long taskId;
    private Integer attemptId;
    private Long jobId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Integer attemptId) {
        this.attemptId = attemptId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
