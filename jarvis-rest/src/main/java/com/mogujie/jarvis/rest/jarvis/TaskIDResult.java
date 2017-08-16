/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月22日 上午11:30:27
 */

package com.mogujie.jarvis.rest.jarvis;

public class TaskIDResult extends Result {
    private static final long serialVersionUID = 901386461875518L;
    private long taskId;
    private Integer errorCode;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

}
