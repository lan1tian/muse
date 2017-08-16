package com.mogujie.jarvis.rest.vo;

import java.util.List;

/**
 * job返回类
 * @author muming
 */
public class TaskRelationsVo extends  AbstractVo{

    private List<RelationEntry> list;

    public List<RelationEntry> getList() {
        return list;
    }
    public void setList(List<RelationEntry> list) {
        this.list = list;
    }

    public static class RelationEntry{
        private long jobId;
        private List<Long> taskIds;

        public long getJobId() {
            return jobId;
        }
        public RelationEntry setJobId(long jobId) {
            this.jobId = jobId;
            return this;
        }

        public List<Long> getTaskIds() {
            return taskIds;
        }

        public RelationEntry setTaskIds(List<Long> taskIds) {
            this.taskIds = taskIds;
            return this;
        }
    }
}
