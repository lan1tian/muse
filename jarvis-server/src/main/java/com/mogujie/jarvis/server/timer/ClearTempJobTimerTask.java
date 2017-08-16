/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月4日 上午10:57:36
 */

package com.mogujie.jarvis.server.timer;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.dto.generate.Job;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.dag.JobGraph;
import com.mogujie.jarvis.server.service.JobService;

/**
 * 临时任务清理定时器，每周一凌晨3点清理之前所有临时任务
 *
 * @author guangming
 *
 */
public class ClearTempJobTimerTask extends AbstractTimerTask {
    private static final Logger LOGGER = LogManager.getLogger();
    private JobService jobService = Injectors.getInjector().getInstance(JobService.class);
    private JobGraph jobGraph = JobGraph.INSTANCE;

    @Override
    public void run() {
        DateTime now = DateTime.now();
        List<Job> jobs = jobService.getTempJobsBefore(now);
        if (jobs != null) {
            for (Job oldJob : jobs) {
                Job newJob = new Job();
                newJob.setJobId(oldJob.getJobId());
                newJob.setStatus(JobStatus.DELETED.getValue());
                newJob.setUpdateTime(now.toDate());
                jobService.updateJob(newJob);
                jobGraph.removeJob(oldJob.getJobId());
            }
        }
        LOGGER.info("clear temp jobs before {}", now);
    }

    @Override
    public DateTime getFirstTime(DateTime currentDateTime) {
        final String startTime = "03:00:00";
        String fristDate = currentDateTime.withDayOfWeek(1).plusWeeks(1).toString("yyyy-MM-dd");
        return new DateTime(fristDate + "T" + startTime);
    }

    @Override
    public long getPeriod() {
        final long oneWeek = 7 * 24 * 60 * 60; //单位：秒
        return oneWeek;
    }
}
