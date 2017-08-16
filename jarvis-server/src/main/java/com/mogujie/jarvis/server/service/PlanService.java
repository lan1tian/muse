/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月2日 下午3:34:47
 */

package com.mogujie.jarvis.server.service;


import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.dao.generate.PlanMapper;
import com.mogujie.jarvis.dto.generate.Plan;
import com.mogujie.jarvis.dto.generate.PlanExample;
import com.mogujie.jarvis.server.util.PlanUtil;

/**
 * @author guangming
 *
 */
@Singleton
public class PlanService {

    @Inject
    private PlanMapper planMapper;

    @Inject
    private JobService jobService;

    public void refreshAllPlan(Range<DateTime> range) {
        //先把所有老的删除
        PlanExample example = new PlanExample();
        example.createCriteria();
        planMapper.deleteByExample(example);

        //重新生成新的plan
        List<Long> activeJobIds = jobService.getEnableActiveJobIds();
        DateTime now = DateTime.now();
        for (long jobId : activeJobIds) {
            DateTime nextTime = PlanUtil.getScheduleTimeAfter(jobId, range.lowerEndpoint().minusSeconds(1));
            while (nextTime != null && range.contains(nextTime)) {
                Plan plan = new Plan();
                plan.setJobId(jobId);
                plan.setPlanTime(nextTime.toDate());
                plan.setCreateTime(now.toDate());
                planMapper.insert(plan);
                nextTime = PlanUtil.getScheduleTimeAfter(jobId, nextTime);
            }
        }
    }

    public void refreshPlan(long jobId, Range<DateTime> range) {
        //先把所有老的删除
        PlanExample example = new PlanExample();
        example.createCriteria().andJobIdEqualTo(jobId);
        planMapper.deleteByExample(example);

        //重新生成新的plan
        DateTime now = DateTime.now();
        DateTime nextTime = PlanUtil.getScheduleTimeAfter(jobId, range.lowerEndpoint().minusSeconds(1));
        while (nextTime != null && range.contains(nextTime)) {
            Plan plan = new Plan();
            plan.setJobId(jobId);
            plan.setPlanTime(nextTime.toDate());
            plan.setCreateTime(now.toDate());
            planMapper.insert(plan);
            nextTime = PlanUtil.getScheduleTimeAfter(jobId, nextTime);
        }
    }

    public void removePlan(long jobId) {
        PlanExample example = new PlanExample();
        example.createCriteria().andJobIdEqualTo(jobId);
        planMapper.deleteByExample(example);
    }
}
