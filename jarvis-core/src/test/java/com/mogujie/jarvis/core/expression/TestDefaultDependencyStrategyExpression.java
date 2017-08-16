/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月8日 上午11:03:03
 */

package com.mogujie.jarvis.core.expression;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TestDefaultDependencyStrategyExpression {

    @Test
    public void testAll() {
        DependencyStrategyExpression expression = new DefaultDependencyStrategyExpression("*");
        Assert.assertTrue(expression.isValid());

        Assert.assertTrue(expression.check(Lists.newArrayList(true, true, true)));
        Assert.assertTrue(!expression.check(Lists.newArrayList(true, false, true)));
    }

    @Test
    public void testAny() {
        DependencyStrategyExpression expression = new DefaultDependencyStrategyExpression("+");
        Assert.assertTrue(expression.isValid());

        Assert.assertTrue(expression.check(Lists.newArrayList(true, true, true)));
        Assert.assertTrue(expression.check(Lists.newArrayList(true, false, true)));
        Assert.assertTrue(!expression.check(Lists.newArrayList(false, false, false)));
    }

    @Test
    public void testLast1() {
        DependencyStrategyExpression expression = new DefaultDependencyStrategyExpression("L(1)");
        Assert.assertTrue(expression.isValid());

        Assert.assertTrue(expression.check(Lists.newArrayList(true, true, true)));
        Assert.assertTrue(expression.check(Lists.newArrayList(true, false, true)));
        Assert.assertTrue(!expression.check(Lists.newArrayList(false, false, false)));
        Assert.assertTrue(!expression.check(Lists.newArrayList(true, false, false)));
    }

    @Test
    public void testLast2() {
        DependencyStrategyExpression expression = new DefaultDependencyStrategyExpression("L(2)");
        Assert.assertTrue(expression.isValid());

        Assert.assertTrue(expression.check(Lists.newArrayList(true, true, true)));
        Assert.assertTrue(!expression.check(Lists.newArrayList(true, false, true)));
        Assert.assertTrue(!expression.check(Lists.newArrayList(false, false, false)));
        Assert.assertTrue(!expression.check(Lists.newArrayList(true, false, false)));
    }

    @Test
    public void testLast3() {
        DependencyStrategyExpression expression = new DefaultDependencyStrategyExpression("L(-2)");
        Assert.assertTrue(!expression.isValid());
    }
}
