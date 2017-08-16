/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月16日 下午4:16:43
 */

package com.mogujie.jarvis.server.scheduler.event;

/**
 * This Event handled by {@link com.mogujie.jarvis.server.scheduler.task.TaskScheduler}.
 * Sent by {@link com.mogujie.jarvis.server.actor.TaskMetricsActor}.
 *
 * 用来处理任务running的事件
 *
 * @author guangming
 *
 */
public class RunningEvent extends DAGTaskEvent {
    private int workerId;

    /**
     * @param jobId
     * @param taskId
     * @param workerId
     */
    public RunningEvent(long jobId, long taskId, int workerId) {
        super(jobId, taskId);
        this.workerId = workerId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

}
