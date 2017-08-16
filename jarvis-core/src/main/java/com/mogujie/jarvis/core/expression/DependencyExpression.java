/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月1日 下午2:22:49
 */

package com.mogujie.jarvis.core.expression;

import org.joda.time.DateTime;

import com.google.common.collect.Range;

public abstract class DependencyExpression implements Expression {
    protected String expression;

    enum OffsetType {
        REVERSE, FRONT;
    }

    public DependencyExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    abstract public Range<DateTime> getRange(DateTime dateTime);

    abstract public Range<DateTime> getReverseRange(DateTime dateTime);
}
