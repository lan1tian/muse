package com.mogujie.jarvis.tasks;

import org.junit.Ignore;
import org.junit.Test;

import com.mogujie.jarvis.core.AbstractLogCollector;
import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.tasks.shell.ShellTask;

/**
 * Created by muming on 16/1/22.
 */
@Ignore
public class TestShellTask {

    @Test
    public void test() {

        String fullId = "2_1_1";
        AbstractLogCollector logCollector = new LocalLogCollector(fullId);

        TaskDetail taskDetail = TaskDetail.newTaskDetailBuilder().setTaskName("test").setContent("pwd;ls").setFullId(fullId).build();
        TaskContext taskContext = TaskContext.newBuilder().setTaskDetail(taskDetail).setLogCollector(logCollector).build();

        ShellTask task = new ShellTask(taskContext);
        task.execute();

    }

}
