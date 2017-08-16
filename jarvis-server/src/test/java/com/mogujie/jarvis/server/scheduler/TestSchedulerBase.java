/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年11月6日 下午5:40:40
 */

package com.mogujie.jarvis.server.scheduler;

import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.mogujie.jarvis.core.domain.IdType;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.core.util.IdUtils;
import com.mogujie.jarvis.server.dispatcher.PriorityTaskQueue;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.dag.DAGScheduler;
import com.mogujie.jarvis.server.scheduler.dag.JobGraph;
import com.mogujie.jarvis.server.scheduler.task.DAGTask;
import com.mogujie.jarvis.server.scheduler.task.TaskGraph;
import com.mogujie.jarvis.server.scheduler.task.TaskScheduler;
import com.mogujie.jarvis.server.scheduler.time.TimePlan;
import com.mogujie.jarvis.server.scheduler.time.TimeScheduler;
import com.mogujie.jarvis.server.service.TaskService;

/**
 * @author guangming
 *
 */
public class TestSchedulerBase {
    protected static DAGScheduler dagScheduler;
    protected static TaskScheduler taskScheduler;
    protected static TimeScheduler timeScheduler;
    protected static JobSchedulerController controller;
    protected static JobGraph jobGraph;
    protected static TaskGraph taskGraph;
    protected static PriorityTaskQueue taskQueue;
    protected static TimePlan plan = TimePlan.INSTANCE;
    protected static Configuration conf = ConfigUtils.getServerConfig();
    protected TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);

    @BeforeClass
    public static void setup() throws Exception {
        conf.clear();
        conf.setProperty(JobSchedulerController.SCHEDULER_CONTROLLER_TYPE, JobSchedulerController.SCHEDULER_CONTROLLER_TYPE_SYNC);
        controller = JobSchedulerController.getInstance();
        dagScheduler = DAGScheduler.getInstance();
        taskScheduler = TaskScheduler.getInstance();
        timeScheduler = TimeScheduler.getInstance();
        controller.register(dagScheduler);
        controller.register(taskScheduler);
        controller.register(timeScheduler);
        jobGraph = JobGraph.INSTANCE;
        taskGraph = TaskGraph.INSTANCE;
        taskQueue = taskScheduler.getTaskQueue();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        controller.unregister(dagScheduler);
        controller.unregister(taskScheduler);
        controller.unregister(timeScheduler);
    }

    @After
    public void tearDown() throws Exception {
        Map<Long, DAGTask> taskMap = taskGraph.getTaskMap();
        for (long taskId : taskMap.keySet()) {
            taskService.deleteTaskAndRelation(taskId);
        }
        while (!taskQueue.isEmpty()) {
            TaskDetail taskDetail = taskQueue.get();
            long taskId = IdUtils.parse(taskDetail.getFullId(), IdType.TASK_ID);
            taskService.deleteTaskAndRelation(taskId);
        }
        dagScheduler.destroy();
        taskScheduler.destroy();
        taskQueue.clear();
        plan.clear();
    }
}
