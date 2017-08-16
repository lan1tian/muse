/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年1月4日 上午10:45:50
 */

package com.mogujie.jarvis.server.scheduler.time;

import com.mogujie.jarvis.dto.generate.Job;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.JobService;
import com.mogujie.jarvis.server.util.PlanUtil;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author guangming
 */
public class TestTimePlan {
    private TimePlan plan = TimePlan.INSTANCE;

    @After
    public void tearDown() {
        plan.clear();
    }

    @Test
    public void testRemovePlan() {
        TimePlanEntry entry1 = new TimePlanEntry(1, new DateTime(1000));
        TimePlanEntry entry2 = new TimePlanEntry(2, new DateTime(2000));
        TimePlanEntry entry3 = new TimePlanEntry(3, new DateTime(3000), 3);

        plan.addPlan(entry2);
        plan.addPlan(entry3);
        plan.addPlan(entry1);
        Assert.assertEquals(3, plan.getPlan().size());

        plan.removePlan(new TimePlanEntry(100, new DateTime(2000)));
        Assert.assertEquals(3, plan.getPlan().size());

        plan.removePlan(new TimePlanEntry(2, new DateTime(2000)));
        Assert.assertEquals(2, plan.getPlan().size());

        plan.removePlan(new TimePlanEntry(0, null, 3));
        Assert.assertEquals(1, plan.getPlan().size());
    }

    @Test
    public void testAddPlan() {
        for (int i = 1; i < 10000; i += 2) {
            TimePlanEntry entry = new TimePlanEntry(i, new DateTime(i * 1000L));
            plan.addPlan(entry);
        }

        for (int i = 2; i < 10000; i += 2) {
            TimePlanEntry entry = new TimePlanEntry(i, new DateTime(i * 1000L));
            plan.addPlan(entry);
        }

        for (int i = 1; i < 10000; i++) {
            System.out.println(plan.getPlan().poll());
        }
    }

    @Test
    public void testAddPlan2() {
        TimePlanEntry entry1 = new TimePlanEntry(1, new DateTime(1000));
        TimePlanEntry entry2 = new TimePlanEntry(2, new DateTime(2000));
        TimePlanEntry entry3 = new TimePlanEntry(3, new DateTime(3000), 3);

        plan.addPlan(entry2);
        plan.addPlan(entry3);
        plan.addPlan(entry1);
        Assert.assertEquals(3, plan.getPlan().size());

        plan.addPlan(new TimePlanEntry(1, new DateTime(1000)));
        Assert.assertEquals(3, plan.getPlan().size());

        plan.removePlan(new TimePlanEntry(1, new DateTime(1000)));
        System.out.println(plan.getPlan().size());
    }

    @Test
    public void removeJob() {
        TimePlanEntry entry1 = new TimePlanEntry(1, new DateTime(1000));
        TimePlanEntry entry2 = new TimePlanEntry(2, new DateTime(2000));
        TimePlanEntry entry3 = new TimePlanEntry(3, new DateTime(3000), 3);
        TimePlanEntry entry4 = new TimePlanEntry(3, new DateTime(2500));
        TimePlanEntry entry5 = new TimePlanEntry(2, new DateTime(4000));

        plan.addPlan(entry2);
        plan.addPlan(entry3);
        plan.addPlan(entry1);
        plan.addPlan(entry4);
        plan.addPlan(entry5);
        Assert.assertEquals(5, plan.getPlan().size());

        plan.removeJob(2);
        Assert.assertEquals(3, plan.getPlan().size());

        plan.removeJob(3);
        Assert.assertEquals(1, plan.getPlan().size());
    }

    @Test
    public void getNextTimeOfAllJobs() {
        JobService jobService = Injectors.getInjector().getInstance(JobService.class);
        List<Job> jobs = jobService.getNotDeletedJobs();
        DateTime now = DateTime.now();
        for (Job job : jobs) {
            DateTime dt = PlanUtil.getScheduleTimeAfter(job.getJobId(), now);
            System.out.println("job " + job.getJobId() + ", " + dt);
        }
    }


    public void testNoScheduleExpJob() {
        long jobId = 318L;
        DateTime now = DateTime.now();
        DateTime dt = PlanUtil.getScheduleTimeAfter(jobId, now);
        System.out.println("job " + jobId + ", " + dt);
    }
}
