package com.mogujie.jarvis.worker.task;

import com.mogujie.jarvis.core.TaskReporter;
import com.mogujie.jarvis.core.domain.TaskDetail;

public class TestTaskReporter implements TaskReporter {

    @Override
    public void report(TaskDetail taskDetail) {
        System.out.println("TaskReporter:\t" + taskDetail);
    }

}
