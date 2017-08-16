/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年1月12日 下午4:23:40
 */

package com.mogujie.jarvis.server.scheduler.time;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.google.common.collect.Range;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.PlanService;
import com.mogujie.jarvis.server.service.TaskService;
import com.mogujie.jarvis.server.util.PlanUtil;

/**
 * The next plan of time based jobs.
 *
 * @author guangming
 *
 */
public enum TimePlan {
    INSTANCE;

    private Comparator<TimePlanEntry> comparator = new Comparator<TimePlanEntry>() {

        @Override
        public int compare(TimePlanEntry o1, TimePlanEntry o2) {
            return o1.getDateTime().compareTo(o2.getDateTime());
        }
    };

    // 优先级队列，通过调度时间由小到大排序
    private Queue<TimePlanEntry> plan = new PriorityBlockingQueue<>(100, comparator);
    private PlanService planService = Injectors.getInjector().getInstance(PlanService.class);
    private static Logger LOGGER = LogManager.getLogger();

    /**
     * add a plan
     *
     * @param entry
     */
    public synchronized boolean addPlan(TimePlanEntry entry) {
        if (!plan.contains(entry)) {
            return plan.add(entry);
        } else {
            return false;
        }
    }

    /**
     * remove a plan
     *
     * @param entry
     */
    public synchronized boolean removePlan(TimePlanEntry planEntry) {
        return plan.remove(planEntry);
    }

    /**
     * add job, add next plan of now
     *
     * @param jobId
     */
    public synchronized void addJob(long jobId) {
        DateTime now = DateTime.now();
        DateTime nextTime = PlanUtil.getScheduleTimeAfter(jobId, now);
        if (nextTime != null) {
            TimePlanEntry planEntry = new TimePlanEntry(jobId, nextTime);
            boolean ret = plan.add(planEntry);
            if (!ret) {
                LOGGER.error("add plan[{}] failed, plan size is {}", planEntry, plan.size());
            }
        } else {
            LOGGER.warn("next time is null, jobId={}, dateTime={}", jobId, now);
        }
    }

    /**
     * recover job, add next plan of history
     *
     * @param jobId
     */
    public synchronized void recoverJob(long jobId) {
        long scheduleTime = DateTime.now().getMillis();
        TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);
        Task lastone = taskService.getLastTask(jobId, scheduleTime, TaskType.SCHEDULE);
        if (lastone != null) {
            scheduleTime = lastone.getScheduleTime().getTime();
        }
        DateTime nextTime = PlanUtil.getScheduleTimeAfter(jobId, new DateTime(scheduleTime));
        if (nextTime != null) {
            plan.add(new TimePlanEntry(jobId, nextTime));
        } else {
            LOGGER.warn("next time is null, jobId={}, dateTime={}", jobId, new DateTime(scheduleTime));
        }
    }

    /**
     * remove job, remove all plan of this job
     *
     * @param jobId
     */
    public synchronized void removeJob(long jobId) {
        Iterator<TimePlanEntry> it = plan.iterator();
        while (it.hasNext()) {
            TimePlanEntry entry = it.next();
            if (entry.getJobId() == jobId) {
                it.remove();
            }
        }
    }

    /**
     * modify job flag
     * 1) ENABLE->DISABLE|DELETED|PAUSE, remove job
     * 2) PAUSE->ENABLE, recover job
     * 3) DISABLE|DELETED->ENABLE, add job
     *
     * @param jobId
     * @param oldStatus
     * @param newStatus
     */
    public synchronized void modifyJobFlag(long jobId, JobStatus oldStatus, JobStatus newStatus) {
        if (newStatus.equals(JobStatus.DISABLE) || newStatus.equals(JobStatus.DELETED)
                || newStatus.equals(JobStatus.PAUSE)) {
            removeJob(jobId);
            if (newStatus.equals(JobStatus.DISABLE) || newStatus.equals(JobStatus.DELETED)) {
                planService.removePlan(jobId);
            }
        } else if (newStatus.equals(JobStatus.ENABLE)) {
            if (oldStatus.equals(JobStatus.PAUSE)) {
                //如果从暂停状态恢复过来，要把之前的没跑过的都恢复回来
                recoverJob(jobId);
            } else if (oldStatus.equals(JobStatus.DISABLE) || oldStatus.equals(JobStatus.DELETED)) {
                //如果是从禁用或废弃状态恢复回来，不需要恢复历史任务，从当前时间计算下一次
                addJob(jobId);
                DateTime now = DateTime.now();
                Range<DateTime> range = Range.closedOpen(now, now.plusDays(1).withTimeAtStartOfDay());
                planService.refreshPlan(jobId, range);
            } else if (oldStatus.equals(JobStatus.ENABLE)) {
                // nothing to do
            }
        }
    }

    public synchronized Queue<TimePlanEntry> getPlan() {
        return plan;
    }

    public synchronized void clear() {
        plan.clear();
    }

}
