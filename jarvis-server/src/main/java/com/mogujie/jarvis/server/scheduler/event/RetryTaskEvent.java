/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年11月18日 下午5:12:39
 */

package com.mogujie.jarvis.server.scheduler.event;


/**
 * This Event handled by {@link com.mogujie.jarvis.server.scheduler.task.TaskScheduler}.
 * Sent by {@link com.mogujie.jarvis.server.actor.TaskActor}
 *
 * 原地重试task处理事件
 *
 * @author guangming
 *
 */
public class RetryTaskEvent extends DAGTaskEvent {
    private String user;

    /**
     * @param jobId
     * @param taskId
     * @param user
     */
    public RetryTaskEvent(long jobId, long taskId, String user) {
        super(jobId, taskId);
        this.user = user;
    }

    /**
     * @param taskId
     * @param user
     */
    public RetryTaskEvent(long taskId, String user) {
        super(taskId);
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
