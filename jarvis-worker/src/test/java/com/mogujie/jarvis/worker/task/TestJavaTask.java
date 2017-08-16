package com.mogujie.jarvis.worker.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.joda.time.DateTime;

import com.mogujie.jarvis.core.AbstractTask;
import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.TaskContext.TaskContextBuilder;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.domain.TaskDetail.TaskDetailBuilder;
import com.mogujie.jarvis.core.exception.TaskException;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.tasks.shell.JavaTask;

public class TestJavaTask {

    public static void main(String[] args) {
        Configuration conf = ConfigUtils.getWorkerConfig();
        conf.addProperty("hdfs.jar.root.path", "/tmp/jarvis");
        conf.addProperty("local.jar.root.path", "/Users/wuya/Desktop/tmp-jarvis");

        TaskDetailBuilder taskBuilder = TaskDetail.newTaskDetailBuilder();
        taskBuilder.setFullId("1_2_3");
        taskBuilder.setTaskName("test_java_task");
        taskBuilder.setAppName("testApp");
        taskBuilder.setUser("wuya");
        taskBuilder.setJobType("java");
        taskBuilder.setContent("");
        taskBuilder.setPriority(5);
        taskBuilder.setDataTime(DateTime.now());

        Map<String, Object> map = new HashMap<>();
        map.put("mainClass", "com.mogujie.test.Test");
        map.put("jar", "test.jar");
        map.put("args", "123 456 a ba c");
        map.put("classpath", "guava-19.0.jar");
        taskBuilder.setParameters(map);

        TaskContextBuilder contextBuilder = TaskContext.newBuilder();
        TaskDetail taskDetail = taskBuilder.build();
        contextBuilder.setTaskDetail(taskDetail);

        contextBuilder.setLogCollector(new ConsoleLogCollector());
        contextBuilder.setProgressReporter(new ConsoleProgressReporter());
        contextBuilder.setTaskReporter(new TestTaskReporter());

        TaskContext context = contextBuilder.build();

        AbstractTask task = new JavaTask(context);
        try {
            task.preExecute();
            System.out.println("Task status: " + task.execute());
            task.postExecute();
        } catch (TaskException e) {
            e.printStackTrace();
        }

    }

}
