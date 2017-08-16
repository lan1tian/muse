/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月1日 上午11:53:44
 */

package com.mogujie.jarvis.server.scheduler.event;

import com.mogujie.jarvis.core.domain.TaskType;

/**
 * This Event handled by {@link com.mogujie.jarvis.server.scheduler.task.TaskScheduler}.
 * Sent by {@link com.mogujie.jarvis.server.actor.TaskMetricsActor}.
 *
 * 用来处理任务成功的事件
 *
 * @author guangming
 *
 */
public class SuccessEvent extends DAGTaskEvent {
    private long scheduleTime;
    private TaskType taskType;
    private String reason;

    /**
     * @param jobId
     * @param taskId
     * @param scheduleTime
     * @param taskType
     * @param reason
     */
    public SuccessEvent(long jobId, long taskId, long scheduleTime, TaskType taskType, String reason) {
        super(jobId, taskId);
        this.scheduleTime = scheduleTime;
        this.taskType = taskType;
        this.reason = reason;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
