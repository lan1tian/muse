/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月16日 下午4:17:46
 */

package com.mogujie.jarvis.server.scheduler.event;

/**
 * This Event handled by {@link com.mogujie.jarvis.server.scheduler.task.TaskScheduler}.
 * Sent by {@link com.mogujie.jarvis.server.actor.TaskMetricsActor}.
 *
 * 用来处理任务killed的事件
 *
 * @author guangming
 *
 */
public class KilledEvent extends DAGTaskEvent {

    /**
     * @param jobId
     * @param taskId
     */
    public KilledEvent(long jobId, long taskId) {
        super(jobId, taskId);
    }

}
