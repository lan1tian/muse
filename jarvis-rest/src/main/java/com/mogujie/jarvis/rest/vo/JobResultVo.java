package com.mogujie.jarvis.rest.vo;

/**
 * job返回类
 * @author muming
 */
public class JobResultVo extends  AbstractVo{

    private long jobId;

    public long getJobId() {
        return jobId;
    }

    public JobResultVo setJobId(long jobId) {
        this.jobId = jobId;
        return this;
    }
}
