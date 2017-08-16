/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月1日 下午2:02:12
 */

package com.mogujie.jarvis.server.scheduler.event;


/**
 * This Event handled by {@link com.mogujie.jarvis.server.scheduler.task.TaskScheduler}.
 * Sent by {@link com.mogujie.jarvis.server.actor.TaskMetricsActor}.
 *
 * 用来处理任务失败的事件
 *
 * @author guangming
 *
 */
public class FailedEvent extends DAGTaskEvent {

    private String reason;

    /**
     * @param  jobId
     * @param  taskId
     * @param  reason
     */
    public FailedEvent(long jobId, long taskId, String reason) {
        super(jobId, taskId);
        this.reason = reason;
    }

    /**
     * @param  taskId
     * @param  reason
     */
    public FailedEvent(long taskId, String reason) {
        super(taskId);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
