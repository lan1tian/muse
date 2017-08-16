package com.mogujie.jarvis.dto.generate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskExample {
    protected String orderByClause;

    protected boolean distinct;

    protected java.util.List<Criteria> oredCriteria;

    public TaskExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public java.util.List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected java.util.List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public java.util.List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andTaskIdIsNull() {
            addCriterion("taskId is null");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNotNull() {
            addCriterion("taskId is not null");
            return (Criteria) this;
        }

        public Criteria andTaskIdEqualTo(Long value) {
            addCriterion("taskId =", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotEqualTo(Long value) {
            addCriterion("taskId <>", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThan(Long value) {
            addCriterion("taskId >", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThanOrEqualTo(Long value) {
            addCriterion("taskId >=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThan(Long value) {
            addCriterion("taskId <", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThanOrEqualTo(Long value) {
            addCriterion("taskId <=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdIn(java.util.List<java.lang.Long> values) {
            addCriterion("taskId in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotIn(java.util.List<java.lang.Long> values) {
            addCriterion("taskId not in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdBetween(Long value1, Long value2) {
            addCriterion("taskId between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotBetween(Long value1, Long value2) {
            addCriterion("taskId not between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdIsNull() {
            addCriterion("attemptId is null");
            return (Criteria) this;
        }

        public Criteria andAttemptIdIsNotNull() {
            addCriterion("attemptId is not null");
            return (Criteria) this;
        }

        public Criteria andAttemptIdEqualTo(Integer value) {
            addCriterion("attemptId =", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdNotEqualTo(Integer value) {
            addCriterion("attemptId <>", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdGreaterThan(Integer value) {
            addCriterion("attemptId >", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("attemptId >=", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdLessThan(Integer value) {
            addCriterion("attemptId <", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdLessThanOrEqualTo(Integer value) {
            addCriterion("attemptId <=", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdIn(java.util.List<java.lang.Integer> values) {
            addCriterion("attemptId in", values, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdNotIn(java.util.List<java.lang.Integer> values) {
            addCriterion("attemptId not in", values, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdBetween(Integer value1, Integer value2) {
            addCriterion("attemptId between", value1, value2, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdNotBetween(Integer value1, Integer value2) {
            addCriterion("attemptId not between", value1, value2, "attemptId");
            return (Criteria) this;
        }

        public Criteria andJobIdIsNull() {
            addCriterion("jobId is null");
            return (Criteria) this;
        }

        public Criteria andJobIdIsNotNull() {
            addCriterion("jobId is not null");
            return (Criteria) this;
        }

        public Criteria andJobIdEqualTo(Long value) {
            addCriterion("jobId =", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotEqualTo(Long value) {
            addCriterion("jobId <>", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdGreaterThan(Long value) {
            addCriterion("jobId >", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdGreaterThanOrEqualTo(Long value) {
            addCriterion("jobId >=", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLessThan(Long value) {
            addCriterion("jobId <", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLessThanOrEqualTo(Long value) {
            addCriterion("jobId <=", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdIn(java.util.List<java.lang.Long> values) {
            addCriterion("jobId in", values, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotIn(java.util.List<java.lang.Long> values) {
            addCriterion("jobId not in", values, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdBetween(Long value1, Long value2) {
            addCriterion("jobId between", value1, value2, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotBetween(Long value1, Long value2) {
            addCriterion("jobId not between", value1, value2, "jobId");
            return (Criteria) this;
        }

        public Criteria andParamsIsNull() {
            addCriterion("params is null");
            return (Criteria) this;
        }

        public Criteria andParamsIsNotNull() {
            addCriterion("params is not null");
            return (Criteria) this;
        }

        public Criteria andParamsEqualTo(String value) {
            addCriterion("params =", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotEqualTo(String value) {
            addCriterion("params <>", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsGreaterThan(String value) {
            addCriterion("params >", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsGreaterThanOrEqualTo(String value) {
            addCriterion("params >=", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsLessThan(String value) {
            addCriterion("params <", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsLessThanOrEqualTo(String value) {
            addCriterion("params <=", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsLike(String value) {
            addCriterion("params like", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotLike(String value) {
            addCriterion("params not like", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsIn(java.util.List<java.lang.String> values) {
            addCriterion("params in", values, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotIn(java.util.List<java.lang.String> values) {
            addCriterion("params not in", values, "params");
            return (Criteria) this;
        }

        public Criteria andParamsBetween(String value1, String value2) {
            addCriterion("params between", value1, value2, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotBetween(String value1, String value2) {
            addCriterion("params not between", value1, value2, "params");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeIsNull() {
            addCriterion("scheduleTime is null");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeIsNotNull() {
            addCriterion("scheduleTime is not null");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeEqualTo(Date value) {
            addCriterion("scheduleTime =", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeNotEqualTo(Date value) {
            addCriterion("scheduleTime <>", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeGreaterThan(Date value) {
            addCriterion("scheduleTime >", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("scheduleTime >=", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeLessThan(Date value) {
            addCriterion("scheduleTime <", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeLessThanOrEqualTo(Date value) {
            addCriterion("scheduleTime <=", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeIn(java.util.List<java.util.Date> values) {
            addCriterion("scheduleTime in", values, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeNotIn(java.util.List<java.util.Date> values) {
            addCriterion("scheduleTime not in", values, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeBetween(Date value1, Date value2) {
            addCriterion("scheduleTime between", value1, value2, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeNotBetween(Date value1, Date value2) {
            addCriterion("scheduleTime not between", value1, value2, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeIsNull() {
            addCriterion("dataTime is null");
            return (Criteria) this;
        }

        public Criteria andDataTimeIsNotNull() {
            addCriterion("dataTime is not null");
            return (Criteria) this;
        }

        public Criteria andDataTimeEqualTo(Date value) {
            addCriterion("dataTime =", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeNotEqualTo(Date value) {
            addCriterion("dataTime <>", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeGreaterThan(Date value) {
            addCriterion("dataTime >", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("dataTime >=", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeLessThan(Date value) {
            addCriterion("dataTime <", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeLessThanOrEqualTo(Date value) {
            addCriterion("dataTime <=", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeIn(java.util.List<java.util.Date> values) {
            addCriterion("dataTime in", values, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeNotIn(java.util.List<java.util.Date> values) {
            addCriterion("dataTime not in", values, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeBetween(Date value1, Date value2) {
            addCriterion("dataTime between", value1, value2, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeNotBetween(Date value1, Date value2) {
            addCriterion("dataTime not between", value1, value2, "dataTime");
            return (Criteria) this;
        }

        public Criteria andProgressIsNull() {
            addCriterion("progress is null");
            return (Criteria) this;
        }

        public Criteria andProgressIsNotNull() {
            addCriterion("progress is not null");
            return (Criteria) this;
        }

        public Criteria andProgressEqualTo(Float value) {
            addCriterion("progress =", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotEqualTo(Float value) {
            addCriterion("progress <>", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressGreaterThan(Float value) {
            addCriterion("progress >", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressGreaterThanOrEqualTo(Float value) {
            addCriterion("progress >=", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressLessThan(Float value) {
            addCriterion("progress <", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressLessThanOrEqualTo(Float value) {
            addCriterion("progress <=", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressIn(java.util.List<java.lang.Float> values) {
            addCriterion("progress in", values, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotIn(java.util.List<java.lang.Float> values) {
            addCriterion("progress not in", values, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressBetween(Float value1, Float value2) {
            addCriterion("progress between", value1, value2, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotBetween(Float value1, Float value2) {
            addCriterion("progress not between", value1, value2, "progress");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(java.util.List<java.lang.Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(java.util.List<java.lang.Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(java.util.List<java.lang.Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(java.util.List<java.lang.Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andFinishReasonIsNull() {
            addCriterion("finishReason is null");
            return (Criteria) this;
        }

        public Criteria andFinishReasonIsNotNull() {
            addCriterion("finishReason is not null");
            return (Criteria) this;
        }

        public Criteria andFinishReasonEqualTo(String value) {
            addCriterion("finishReason =", value, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonNotEqualTo(String value) {
            addCriterion("finishReason <>", value, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonGreaterThan(String value) {
            addCriterion("finishReason >", value, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonGreaterThanOrEqualTo(String value) {
            addCriterion("finishReason >=", value, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonLessThan(String value) {
            addCriterion("finishReason <", value, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonLessThanOrEqualTo(String value) {
            addCriterion("finishReason <=", value, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonLike(String value) {
            addCriterion("finishReason like", value, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonNotLike(String value) {
            addCriterion("finishReason not like", value, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonIn(java.util.List<java.lang.String> values) {
            addCriterion("finishReason in", values, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonNotIn(java.util.List<java.lang.String> values) {
            addCriterion("finishReason not in", values, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonBetween(String value1, String value2) {
            addCriterion("finishReason between", value1, value2, "finishReason");
            return (Criteria) this;
        }

        public Criteria andFinishReasonNotBetween(String value1, String value2) {
            addCriterion("finishReason not between", value1, value2, "finishReason");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNull() {
            addCriterion("appId is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("appId is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(Integer value) {
            addCriterion("appId =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(Integer value) {
            addCriterion("appId <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(Integer value) {
            addCriterion("appId >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("appId >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(Integer value) {
            addCriterion("appId <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(Integer value) {
            addCriterion("appId <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(java.util.List<java.lang.Integer> values) {
            addCriterion("appId in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(java.util.List<java.lang.Integer> values) {
            addCriterion("appId not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(Integer value1, Integer value2) {
            addCriterion("appId between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(Integer value1, Integer value2) {
            addCriterion("appId not between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdIsNull() {
            addCriterion("workerId is null");
            return (Criteria) this;
        }

        public Criteria andWorkerIdIsNotNull() {
            addCriterion("workerId is not null");
            return (Criteria) this;
        }

        public Criteria andWorkerIdEqualTo(Integer value) {
            addCriterion("workerId =", value, "workerId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdNotEqualTo(Integer value) {
            addCriterion("workerId <>", value, "workerId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdGreaterThan(Integer value) {
            addCriterion("workerId >", value, "workerId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("workerId >=", value, "workerId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdLessThan(Integer value) {
            addCriterion("workerId <", value, "workerId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdLessThanOrEqualTo(Integer value) {
            addCriterion("workerId <=", value, "workerId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdIn(java.util.List<java.lang.Integer> values) {
            addCriterion("workerId in", values, "workerId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdNotIn(java.util.List<java.lang.Integer> values) {
            addCriterion("workerId not in", values, "workerId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdBetween(Integer value1, Integer value2) {
            addCriterion("workerId between", value1, value2, "workerId");
            return (Criteria) this;
        }

        public Criteria andWorkerIdNotBetween(Integer value1, Integer value2) {
            addCriterion("workerId not between", value1, value2, "workerId");
            return (Criteria) this;
        }

        public Criteria andExecuteUserIsNull() {
            addCriterion("executeUser is null");
            return (Criteria) this;
        }

        public Criteria andExecuteUserIsNotNull() {
            addCriterion("executeUser is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteUserEqualTo(String value) {
            addCriterion("executeUser =", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserNotEqualTo(String value) {
            addCriterion("executeUser <>", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserGreaterThan(String value) {
            addCriterion("executeUser >", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserGreaterThanOrEqualTo(String value) {
            addCriterion("executeUser >=", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserLessThan(String value) {
            addCriterion("executeUser <", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserLessThanOrEqualTo(String value) {
            addCriterion("executeUser <=", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserLike(String value) {
            addCriterion("executeUser like", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserNotLike(String value) {
            addCriterion("executeUser not like", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserIn(java.util.List<java.lang.String> values) {
            addCriterion("executeUser in", values, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserNotIn(java.util.List<java.lang.String> values) {
            addCriterion("executeUser not in", values, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserBetween(String value1, String value2) {
            addCriterion("executeUser between", value1, value2, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserNotBetween(String value1, String value2) {
            addCriterion("executeUser not between", value1, value2, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeIsNull() {
            addCriterion("executeStartTime is null");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeIsNotNull() {
            addCriterion("executeStartTime is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeEqualTo(Date value) {
            addCriterion("executeStartTime =", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeNotEqualTo(Date value) {
            addCriterion("executeStartTime <>", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeGreaterThan(Date value) {
            addCriterion("executeStartTime >", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("executeStartTime >=", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeLessThan(Date value) {
            addCriterion("executeStartTime <", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("executeStartTime <=", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeIn(java.util.List<java.util.Date> values) {
            addCriterion("executeStartTime in", values, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeNotIn(java.util.List<java.util.Date> values) {
            addCriterion("executeStartTime not in", values, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeBetween(Date value1, Date value2) {
            addCriterion("executeStartTime between", value1, value2, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("executeStartTime not between", value1, value2, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeIsNull() {
            addCriterion("executeEndTime is null");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeIsNotNull() {
            addCriterion("executeEndTime is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeEqualTo(Date value) {
            addCriterion("executeEndTime =", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeNotEqualTo(Date value) {
            addCriterion("executeEndTime <>", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeGreaterThan(Date value) {
            addCriterion("executeEndTime >", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("executeEndTime >=", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeLessThan(Date value) {
            addCriterion("executeEndTime <", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("executeEndTime <=", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeIn(java.util.List<java.util.Date> values) {
            addCriterion("executeEndTime in", values, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeNotIn(java.util.List<java.util.Date> values) {
            addCriterion("executeEndTime not in", values, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeBetween(Date value1, Date value2) {
            addCriterion("executeEndTime between", value1, value2, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("executeEndTime not between", value1, value2, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("createTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("createTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("createTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("createTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("createTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("createTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("createTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(java.util.List<java.util.Date> values) {
            addCriterion("createTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(java.util.List<java.util.Date> values) {
            addCriterion("createTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("createTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("createTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("updateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("updateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("updateTime =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("updateTime <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("updateTime >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updateTime >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("updateTime <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("updateTime <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(java.util.List<java.util.Date> values) {
            addCriterion("updateTime in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(java.util.List<java.util.Date> values) {
            addCriterion("updateTime not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("updateTime between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("updateTime not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableIsNull() {
            addCriterion("alarmEnable is null");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableIsNotNull() {
            addCriterion("alarmEnable is not null");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableEqualTo(Integer value) {
            addCriterion("alarmEnable =", value, "alarmEnable");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableNotEqualTo(Integer value) {
            addCriterion("alarmEnable <>", value, "alarmEnable");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableGreaterThan(Integer value) {
            addCriterion("alarmEnable >", value, "alarmEnable");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableGreaterThanOrEqualTo(Integer value) {
            addCriterion("alarmEnable >=", value, "alarmEnable");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableLessThan(Integer value) {
            addCriterion("alarmEnable <", value, "alarmEnable");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableLessThanOrEqualTo(Integer value) {
            addCriterion("alarmEnable <=", value, "alarmEnable");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableIn(java.util.List<java.lang.Integer> values) {
            addCriterion("alarmEnable in", values, "alarmEnable");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableNotIn(java.util.List<java.lang.Integer> values) {
            addCriterion("alarmEnable not in", values, "alarmEnable");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableBetween(Integer value1, Integer value2) {
            addCriterion("alarmEnable between", value1, value2, "alarmEnable");
            return (Criteria) this;
        }

        public Criteria andAlarmEnableNotBetween(Integer value1, Integer value2) {
            addCriterion("alarmEnable not between", value1, value2, "alarmEnable");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}