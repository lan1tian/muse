/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年12月3日 下午1:46:01
 */

package com.mogujie.jarvis.rest.vo;

/**
 * @author guangming
 *
 */
public class TaskResultVo extends  AbstractVo{
    private long taskId;
    private int status;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
