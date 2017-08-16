/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 上午11:38:28
 */

package com.mogujie.jarvis.server.scheduler.dag;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.dag.checker.DAGDependChecker;
import com.mogujie.jarvis.server.service.JobService;

/**
 * @author guangming
 *
 */
public class DAGJob  {

    private long jobId;
    private DAGDependChecker dependChecker;
    private DAGJobType type;
    private JobGraph jobGraph = JobGraph.INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger();

    public DAGJob(long jobId, DAGJobType type) {
        this.jobId = jobId;
        this.type = type;
        this.dependChecker = new DAGDependChecker(jobId);
    }

    /**
     * check dependency with scheduleTime
     *
     * @param scheduleTime
     */
    public boolean checkDependency(long scheduleTime) {
        boolean dependCheck = true;
        if (type.implies(DAGJobType.DEPEND)) {
            Set<Long> needJobs = jobGraph.getParentJobIds(this);
            dependCheck = dependChecker.checkDependency(needJobs, scheduleTime);
            if (!dependCheck) {
                LOGGER.debug("dependChecker failed, job {}, needJobs {}", jobId, needJobs);
            }
        }

        return dependCheck;
    }

    /**
     * get depend taskId map
     *
     */
    public Map<Long, List<Long>> getDependTaskIdMap(long scheduleTime) {
        return dependChecker.getDependTaskIdMap(scheduleTime);
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
        this.dependChecker.setMyJobId(jobId);
    }

    public DAGDependChecker getDependChecker() {
        return dependChecker;
    }

    public void setDependChecker(DAGDependChecker dependChecker) {
        this.dependChecker = dependChecker;
    }

    public DAGJobType getType() {
        return type;
    }

    public void setType(DAGJobType type) {
        this.type = type;
    }

    public JobStatus getJobStatus() {
        JobService jobService = Injectors.getInjector().getInstance(JobService.class);
        JobStatus status = JobStatus.parseValue(jobService.get(jobId).getJob().getStatus());
        return status;
    }

    public void updateJobTypeByTimeFlag(boolean timeFlag) {
        updateJobType(timeFlag, DAGJobType.TIME);
    }

    public void updateJobTypeByDependFlag(boolean dependFlag) {
        updateJobType(dependFlag, DAGJobType.DEPEND);
    }

    public void updateJobTypeByCycleFlag(boolean cycleFlag) {
        updateJobType(cycleFlag, DAGJobType.CYCLE);
    }

    private void updateJobType(boolean isAdd, DAGJobType that) {
        if (isAdd) {
            type = type.or(that);
        } else {
            type = type.remove(that);
        }
    }

    @Override
    public String toString() {
        return "DAGJob [jobId=" + jobId + ", type=" + type + "]";
    }

}
