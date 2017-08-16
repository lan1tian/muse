/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月23日 下午1:15:59
 */

package com.mogujie.jarvis.rest.jarvis;

import java.util.ArrayList;
import java.util.List;

import com.mogujie.jarvis.protocol.JobInfoEntryProtos.JobInfoEntry;

public class TasksResult extends Result {

    private static final long serialVersionUID = 291430869192344650L;
    private List<JobInfo> tasks;

    public TasksResult() {}

    public TasksResult(List<JobInfoEntry> jobInfos) {
        this.tasks = new ArrayList<JobInfo>();
        for (JobInfoEntry jobInfoEntry : jobInfos) {
            this.tasks.add(new JobInfo(jobInfoEntry));
        }
    }

    public List<JobInfo> getTasks() {
        return tasks;
    }

    public void setTasks(List<JobInfo> tasks) {
        this.tasks = tasks;
    }

}

