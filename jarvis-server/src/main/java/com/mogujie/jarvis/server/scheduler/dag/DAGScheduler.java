/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 上午10:50:07
 */

package com.mogujie.jarvis.server.scheduler.dag;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.google.common.eventbus.Subscribe;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.server.scheduler.Scheduler;
import com.mogujie.jarvis.server.scheduler.event.AddPlanEvent;
import com.mogujie.jarvis.server.scheduler.event.ScheduleEvent;
import com.mogujie.jarvis.server.scheduler.event.StartEvent;
import com.mogujie.jarvis.server.scheduler.event.StopEvent;

/**
 * Scheduler used to handle dependency based jobs.
 *
 * @author guangming
 *
 */
public class DAGScheduler extends Scheduler {
    private static DAGScheduler instance = new DAGScheduler();
    private DAGScheduler() {}
    public static DAGScheduler getInstance() {
        return instance;
    }

    private JobGraph jobGraph = JobGraph.INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger();

    public void destroy() {
        jobGraph.clear();
    }

    @Override
    public void handleStartEvent(StartEvent event) {
    }

    @Override
    public void handleStopEvent(StopEvent event) {
    }

    /**
     * 由TaskScheduler发送ScheduleEvent，DAGScheduler进行处理。
     *
     * @param e
     */
    @Subscribe
    public void handleScheduleEvent(ScheduleEvent e) {
        long jobId = e.getJobId();
        long taskId = e.getTaskId();
        long scheduleTime = e.getScheduleTime();
        LOGGER.info("start handleScheduleEvent, jobId={}, scheduleTime={}, taskId={}",
                jobId, new DateTime(scheduleTime), taskId);
        DAGJob dagJob = jobGraph.getDAGJob(jobId);
        if (dagJob != null) {
            List<DAGJob> children = jobGraph.getActiveChildren(dagJob);
            // 如果有子任务，触发子任务
            if (children != null && !children.isEmpty()) {
                for (DAGJob child : children) {
                    if (child.getJobStatus().equals(JobStatus.ENABLE)) {
                        jobGraph.submitJobWithCheck(child, scheduleTime, jobId, taskId);
                    }
                }
            } else if (dagJob.getType().equals(DAGJobType.CYCLE)) {
                // 如果是固定延迟任务，在这里触发
                long fixedPeriod = 10*60; //TODO
                long nextScheduleTime = DateTime.now().getMillis() + fixedPeriod * 1000;
                AddPlanEvent event = new AddPlanEvent(jobId, nextScheduleTime);
                getSchedulerController().notify(event);
            }
        }
    }

    public JobGraph getJobGraph() {
        return jobGraph;
    }

}