/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年12月2日 上午10:09:13
 */

package com.mogujie.jarvis.tasks;

import com.mogujie.jarvis.core.AbstractTask;
import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.exception.TaskException;
import com.mogujie.jarvis.core.util.ThreadUtils;

/**
 * @author guangming
 *
 */
public class DummyTask extends AbstractTask {

    /**
     * @param taskContext
     */
    public DummyTask(TaskContext taskContext) {
        super(taskContext);
    }

    @Override
    public boolean execute() throws TaskException {
        ThreadUtils.sleep(2000);
        return true;
    }

    @Override
    public boolean kill() throws TaskException {
        return true;
    }

}
