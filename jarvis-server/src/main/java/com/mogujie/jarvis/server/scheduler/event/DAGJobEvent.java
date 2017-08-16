/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月1日 下午1:58:37
 */

package com.mogujie.jarvis.server.scheduler.event;

import com.mogujie.jarvis.core.observer.Event;


/**
 * @author guangming
 *
 */
public abstract class DAGJobEvent implements Event {
    private long jobId;

    public DAGJobEvent(long jobId) {
        this.jobId = jobId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }
}
