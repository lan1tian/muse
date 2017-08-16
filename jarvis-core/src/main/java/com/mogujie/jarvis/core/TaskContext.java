/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午2:54:51
 */

package com.mogujie.jarvis.core;

import com.mogujie.jarvis.core.domain.TaskDetail;

/**
 * @author wuya
 *
 */
public final class TaskContext {

    private TaskDetail taskDetail;
    private AbstractLogCollector logCollector;
    private ProgressReporter progressReporter;
    private TaskReporter taskReporter;

    private TaskContext() {
    }

    public static TaskContextBuilder newBuilder() {
        return new TaskContextBuilder();
    }

    public TaskDetail getTaskDetail() {
        return taskDetail;
    }

    public AbstractLogCollector getLogCollector() {
        return logCollector;
    }

    public ProgressReporter getProgressReporter() {
        return progressReporter;
    }

    public TaskReporter getTaskReporter() {
        return taskReporter;
    }

    public static final class TaskContextBuilder {

        private TaskContext taskContext = new TaskContext();

        private TaskContextBuilder() {
        }

        public TaskContextBuilder setTaskDetail(TaskDetail taskDetail) {
            this.taskContext.taskDetail = taskDetail;
            return this;
        }

        public TaskContextBuilder setLogCollector(AbstractLogCollector logCollector) {
            this.taskContext.logCollector = logCollector;
            return this;
        }

        public TaskContextBuilder setProgressReporter(ProgressReporter progressReporter) {
            this.taskContext.progressReporter = progressReporter;
            return this;
        }

        public TaskContextBuilder setTaskReporter(TaskReporter taskReporter) {
            this.taskContext.taskReporter = taskReporter;
            return this;
        }

        public TaskContext build() {
            return this.taskContext;
        }

    }

}
