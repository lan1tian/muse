package com.mogujie.jarvis.rest.vo;

import java.util.List;

/**
 * job返回类
 * @author muming
 */
public class JobRelationsVo extends  AbstractVo{

    private List<JobStatusEntry> list;

    public List<JobStatusEntry> getList() {
        return list;
    }
    public void setList(List<JobStatusEntry> list) {
        this.list = list;
    }
    public static class JobStatusEntry {
        private long jobId;
        private int status;
        public long getJobId() {
            return jobId;
        }
        public JobStatusEntry setJobId(long jobId) {
            this.jobId = jobId;
            return this;
        }
        public int getStatus() {
            return status;
        }
        public JobStatusEntry setStatus(int status) {
            this.status = status;
            return this;
        }
    }
}
