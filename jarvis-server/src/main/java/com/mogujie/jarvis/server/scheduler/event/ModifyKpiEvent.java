/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年3月7日 上午9:55:48
 */

package com.mogujie.jarvis.server.scheduler.event;

/**
 * This Event handled by {@link com.mogujie.jarvis.server.alarm.AlarmScheduler}.
 * Sent by {@link com.mogujie.jarvis.server.actor.JobActor}.
 *
 * 用来处理修改KPI任务的报警事件
 *
 * @author guangming
 *
 */
public class ModifyKpiEvent extends DAGJobEvent {
    private String msg;

    /**
     * @param jobId
     */
    public ModifyKpiEvent(long jobId, String msg) {
        super(jobId);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
