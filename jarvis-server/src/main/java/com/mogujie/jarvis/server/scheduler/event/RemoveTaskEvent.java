/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月25日 下午5:44:58
 */

package com.mogujie.jarvis.server.scheduler.event;

import java.util.List;

import com.mogujie.jarvis.core.observer.Event;


/**
 * This Event handled by {@link com.mogujie.jarvis.server.scheduler.task.TaskScheduler}.
 * Sent by {@link com.mogujie.jarvis.server.actor.TaskMetricsActor}.
 *
 * 用来处理删除task的事件
 *
 * @author guangming
 *
 */
public class RemoveTaskEvent implements Event {
    private List<Long> taskIds;

    /**
     * @param taskIds
     */
    public RemoveTaskEvent(List<Long> taskIds) {
        this.taskIds = taskIds;
    }

    public List<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }

}
