package com.mogujie.jarvis.dto.generate;

public class TaskHistoryKey {
    private Long taskId;

    private Integer attemptId;

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
}