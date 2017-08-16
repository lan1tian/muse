/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月6日 上午11:41:34
 */

package com.mogujie.jarvis.server.domain;

import com.mogujie.jarvis.core.expression.DependencyExpression;
import com.mogujie.jarvis.core.expression.DependencyStrategyExpression;

public class JobDependencyEntry {

    private final DependencyExpression dependencyExpression;
    private final DependencyStrategyExpression dependencyStrategyExpression;

    public JobDependencyEntry(DependencyExpression dependencyExpression, DependencyStrategyExpression dependencyStrategyExpression) {
        this.dependencyExpression = dependencyExpression;
        this.dependencyStrategyExpression = dependencyStrategyExpression;
    }

    public DependencyExpression getDependencyExpression() {
        return dependencyExpression;
    }

    public DependencyStrategyExpression getDependencyStrategyExpression() {
        return dependencyStrategyExpression;
    }

}
