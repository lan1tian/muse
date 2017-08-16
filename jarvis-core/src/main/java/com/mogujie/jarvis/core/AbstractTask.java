/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午2:51:49
 */

package com.mogujie.jarvis.core;

import com.mogujie.jarvis.core.exception.TaskException;

/**
 * @author wuya
 *
 */
public abstract class AbstractTask {

    private final TaskContext taskContext;

    public AbstractTask(TaskContext taskContext) {
        this.taskContext = taskContext;
    }

    public TaskContext getTaskContext() {
        return taskContext;
    }

    public void preExecute() throws TaskException {
    }

    public abstract boolean execute() throws TaskException;

    public void postExecute() throws TaskException {
    }

    public abstract boolean kill() throws TaskException;
}
