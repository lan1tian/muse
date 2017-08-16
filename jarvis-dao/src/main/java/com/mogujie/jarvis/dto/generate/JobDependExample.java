package com.mogujie.jarvis.dto.generate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobDependExample {
    protected String orderByClause;

    protected boolean distinct;

    protected java.util.List<Criteria> oredCriteria;

    public JobDependExample() {
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

        public Criteria andPreJobIdIsNull() {
            addCriterion("preJobId is null");
            return (Criteria) this;
        }

        public Criteria andPreJobIdIsNotNull() {
            addCriterion("preJobId is not null");
            return (Criteria) this;
        }

        public Criteria andPreJobIdEqualTo(Long value) {
            addCriterion("preJobId =", value, "preJobId");
            return (Criteria) this;
        }

        public Criteria andPreJobIdNotEqualTo(Long value) {
            addCriterion("preJobId <>", value, "preJobId");
            return (Criteria) this;
        }

        public Criteria andPreJobIdGreaterThan(Long value) {
            addCriterion("preJobId >", value, "preJobId");
            return (Criteria) this;
        }

        public Criteria andPreJobIdGreaterThanOrEqualTo(Long value) {
            addCriterion("preJobId >=", value, "preJobId");
            return (Criteria) this;
        }

        public Criteria andPreJobIdLessThan(Long value) {
            addCriterion("preJobId <", value, "preJobId");
            return (Criteria) this;
        }

        public Criteria andPreJobIdLessThanOrEqualTo(Long value) {
            addCriterion("preJobId <=", value, "preJobId");
            return (Criteria) this;
        }

        public Criteria andPreJobIdIn(java.util.List<java.lang.Long> values) {
            addCriterion("preJobId in", values, "preJobId");
            return (Criteria) this;
        }

        public Criteria andPreJobIdNotIn(java.util.List<java.lang.Long> values) {
            addCriterion("preJobId not in", values, "preJobId");
            return (Criteria) this;
        }

        public Criteria andPreJobIdBetween(Long value1, Long value2) {
            addCriterion("preJobId between", value1, value2, "preJobId");
            return (Criteria) this;
        }

        public Criteria andPreJobIdNotBetween(Long value1, Long value2) {
            addCriterion("preJobId not between", value1, value2, "preJobId");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyIsNull() {
            addCriterion("commonStrategy is null");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyIsNotNull() {
            addCriterion("commonStrategy is not null");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyEqualTo(Integer value) {
            addCriterion("commonStrategy =", value, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyNotEqualTo(Integer value) {
            addCriterion("commonStrategy <>", value, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyGreaterThan(Integer value) {
            addCriterion("commonStrategy >", value, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyGreaterThanOrEqualTo(Integer value) {
            addCriterion("commonStrategy >=", value, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyLessThan(Integer value) {
            addCriterion("commonStrategy <", value, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyLessThanOrEqualTo(Integer value) {
            addCriterion("commonStrategy <=", value, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyIn(java.util.List<java.lang.Integer> values) {
            addCriterion("commonStrategy in", values, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyNotIn(java.util.List<java.lang.Integer> values) {
            addCriterion("commonStrategy not in", values, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyBetween(Integer value1, Integer value2) {
            addCriterion("commonStrategy between", value1, value2, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andCommonStrategyNotBetween(Integer value1, Integer value2) {
            addCriterion("commonStrategy not between", value1, value2, "commonStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyIsNull() {
            addCriterion("offsetStrategy is null");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyIsNotNull() {
            addCriterion("offsetStrategy is not null");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyEqualTo(String value) {
            addCriterion("offsetStrategy =", value, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyNotEqualTo(String value) {
            addCriterion("offsetStrategy <>", value, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyGreaterThan(String value) {
            addCriterion("offsetStrategy >", value, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyGreaterThanOrEqualTo(String value) {
            addCriterion("offsetStrategy >=", value, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyLessThan(String value) {
            addCriterion("offsetStrategy <", value, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyLessThanOrEqualTo(String value) {
            addCriterion("offsetStrategy <=", value, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyLike(String value) {
            addCriterion("offsetStrategy like", value, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyNotLike(String value) {
            addCriterion("offsetStrategy not like", value, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyIn(java.util.List<java.lang.String> values) {
            addCriterion("offsetStrategy in", values, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyNotIn(java.util.List<java.lang.String> values) {
            addCriterion("offsetStrategy not in", values, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyBetween(String value1, String value2) {
            addCriterion("offsetStrategy between", value1, value2, "offsetStrategy");
            return (Criteria) this;
        }

        public Criteria andOffsetStrategyNotBetween(String value1, String value2) {
            addCriterion("offsetStrategy not between", value1, value2, "offsetStrategy");
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

        public Criteria andUpdateUserIsNull() {
            addCriterion("updateUser is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNotNull() {
            addCriterion("updateUser is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserEqualTo(String value) {
            addCriterion("updateUser =", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotEqualTo(String value) {
            addCriterion("updateUser <>", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThan(String value) {
            addCriterion("updateUser >", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThanOrEqualTo(String value) {
            addCriterion("updateUser >=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThan(String value) {
            addCriterion("updateUser <", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThanOrEqualTo(String value) {
            addCriterion("updateUser <=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLike(String value) {
            addCriterion("updateUser like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotLike(String value) {
            addCriterion("updateUser not like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIn(java.util.List<java.lang.String> values) {
            addCriterion("updateUser in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotIn(java.util.List<java.lang.String> values) {
            addCriterion("updateUser not in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserBetween(String value1, String value2) {
            addCriterion("updateUser between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotBetween(String value1, String value2) {
            addCriterion("updateUser not between", value1, value2, "updateUser");
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