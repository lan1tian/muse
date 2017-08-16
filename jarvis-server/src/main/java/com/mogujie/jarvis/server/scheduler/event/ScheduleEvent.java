/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年10月30日 上午11:04:59
 */

package com.mogujie.jarvis.server.scheduler.event;

/**
 * This Event handled by {@link com.mogujie.jarvis.server.scheduler.dag.DAGScheduler}.
 * Sent by {@link com.mogujie.jarvis.server.scheduler.task.TaskScheduler}.
 *
 * 用来调度子任务，默认childJobId=0，表示调度所有子任务。
 * 如果给定childJobId，则只调度该任务，未来可以用来重跑某一条链路。
 *
 * @author guangming
 *
 */
public class ScheduleEvent extends DAGTaskEvent {
    private long scheduleTime;
    private long childJobId;

    public ScheduleEvent(long jobId, long taskId, long scheduleTime) {
        super(jobId, taskId);
        this.scheduleTime = scheduleTime;
        this.childJobId = 0;
    }

    public ScheduleEvent(long jobId, long taskId, long scheduleTime, long childJobId) {
        super(jobId, taskId);
        this.scheduleTime = scheduleTime;
        this.childJobId = childJobId;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public long getChildJobId() {
        return childJobId;
    }

    public void setChildJobId(long childJobId) {
        this.childJobId = childJobId;
    }

}
