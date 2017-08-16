/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月25日 上午10:49:51
 */

package com.mogujie.jarvis.server.service;

import org.junit.Test;

import com.mogujie.jarvis.dto.generate.Job;
import com.mogujie.jarvis.server.guice.Injectors;

/**
 * @author guangming
 *
 */
public class TestJobService {
    private JobService jobService = Injectors.getInjector().getInstance(JobService.class);

    @Test
    public void testSearchJobByScriptId() {
        Job job = jobService.searchJobByScriptId(478);
        System.out.println(job.getJobId());
    }
}
