/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月12日 下午1:31:18
 */

package com.mogujie.jarvis.worker.domain;

import java.util.List;

import com.mogujie.jarvis.worker.strategy.AcceptanceStrategy;

public class TaskEntry {

    private final String taskType;
    private final String taskClass;
    private final List<AcceptanceStrategy> acceptanceStrategy;
    private final String taskStatusLookupClass;

    public TaskEntry(String taskType, String taskClass, List<AcceptanceStrategy> acceptanceStrategy, String taskStatusLookupClass) {
        this.taskType = taskType;
        this.taskClass = taskClass;
        this.acceptanceStrategy = acceptanceStrategy;
        this.taskStatusLookupClass = taskStatusLookupClass;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public List<AcceptanceStrategy> getAcceptanceStrategy() {
        return acceptanceStrategy;
    }

    public String getTaskStatusLookupClass() {
        return taskStatusLookupClass;
    }

}
