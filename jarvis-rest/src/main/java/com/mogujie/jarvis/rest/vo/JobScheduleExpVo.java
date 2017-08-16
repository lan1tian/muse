package com.mogujie.jarvis.rest.vo;

import java.util.List;

/**
 * job返回类
 * @author muming
 */
public class JobScheduleExpVo extends AbstractVo {

    private long jobId;
    private List<ScheduleExpressionEntry> scheduleExpressionList;

    public static class ScheduleExpressionEntry {
        private Integer operatorMode;
        private Long expressionId;
        private Integer expressionType;
        private String expression;

        public Integer getOperatorMode() {
            return operatorMode;
        }

        public void setOperatorMode(Integer operatorMode) {
            this.operatorMode = operatorMode;
        }

        public Long getExpressionId() {
            return expressionId;
        }

        public void setExpressionId(Long expressionId) {
            this.expressionId = expressionId;
        }

        public Integer getExpressionType() {
            return expressionType;
        }

        public void setExpressionType(Integer expressionType) {
            this.expressionType = expressionType;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public List<ScheduleExpressionEntry> getScheduleExpressionList() {
        return scheduleExpressionList;
    }

    public void setScheduleExpressionList(List<ScheduleExpressionEntry> scheduleExpressionList) {
        this.scheduleExpressionList = scheduleExpressionList;
    }

}
