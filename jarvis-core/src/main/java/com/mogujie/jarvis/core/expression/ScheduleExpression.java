/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年10月29日 下午10:36:27
 */

package com.mogujie.jarvis.core.expression;

import org.joda.time.DateTime;

public abstract class ScheduleExpression implements Expression {

    protected String expression;

    public ScheduleExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expression == null) ? 0 : expression.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScheduleExpression other = (ScheduleExpression) obj;
        if (expression == null) {
            if (other.expression != null)
                return false;
        } else if (!expression.equals(other.expression))
            return false;
        return true;
    }

    abstract public DateTime getTimeBefore(DateTime dateTime);

    abstract public DateTime getTimeAfter(DateTime dateTime);
}
