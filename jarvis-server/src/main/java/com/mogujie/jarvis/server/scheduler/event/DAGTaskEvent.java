/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月1日 下午2:00:30
 */

package com.mogujie.jarvis.server.scheduler.event;


/**
 * @author guangming
 *
 */
public abstract class DAGTaskEvent extends DAGJobEvent {
    private long taskId;

    public DAGTaskEvent(long jobId, long taskId) {
        super(jobId);
        this.taskId = taskId;
    }

    public DAGTaskEvent(long taskId) {
        this(0, taskId);
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}
