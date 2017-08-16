/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月6日 上午10:58:52
 */

package com.mogujie.jarvis.core.expression;

import java.util.List;

public abstract class DependencyStrategyExpression implements Expression {

    protected String expression;

    public DependencyStrategyExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    abstract public boolean check(List<Boolean> list);
}
