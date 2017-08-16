package com.mogujie.jarvis.rest.vo;

import java.util.List;

/**
 * job返回类
 * @author muming
 */
public class JobDependencyVo extends AbstractVo {

    private long jobId;
    private List<DependencyEntry> dependencyList;

    public static class DependencyEntry {
        private Integer operatorMode;
        private Long preJobId;
        private Integer commonStrategy;
        private String offsetStrategy;

        public Integer getOperatorMode() {
            return operatorMode;
        }

        public void setOperatorMode(Integer operatorMode) {
            this.operatorMode = operatorMode;
        }

        public Integer getCommonStrategy() {
            return commonStrategy;
        }

        public void setCommonStrategy(Integer commonStrategy) {
            this.commonStrategy = commonStrategy;
        }

        public String getOffsetStrategy() {
            return offsetStrategy;
        }

        public void setOffsetStrategy(String offsetStrategy) {
            this.offsetStrategy = offsetStrategy;
        }

        public Long getPreJobId() {
            return preJobId;
        }

        public void setPreJobId(Long preJobId) {
            this.preJobId = preJobId;
        }
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public List<DependencyEntry> getDependencyList() {
        return dependencyList;
    }

    public void setDependencyList(List<DependencyEntry> dependencyList) {
        this.dependencyList = dependencyList;
    }

}
