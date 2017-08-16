/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年3月10日 下午3:47:33
 */

package com.mogujie.jarvis.rest.jarvis;

import java.util.ArrayList;
import java.util.List;

import com.mogujie.jarvis.protocol.SearchJobProtos.SimpleJobInfoEntry;

/**
 * @author guangming
 *
 */
public class SimpleTasksResult extends Result {

    private static final long serialVersionUID = 291430869192344650L;

    private List<SimpleJobInfo> tasks;

    public SimpleTasksResult() {}

    public SimpleTasksResult(List<SimpleJobInfoEntry> jobInfos) {
        this.tasks = new ArrayList<SimpleJobInfo>();
        for (SimpleJobInfoEntry jobInfoEntry : jobInfos) {
            this.tasks.add(new SimpleJobInfo(jobInfoEntry));
        }
    }

    public List<SimpleJobInfo> getTasks() {
        return tasks;
    }

    public void setTasks(List<SimpleJobInfo> tasks) {
        this.tasks = tasks;
    }
}
