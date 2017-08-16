/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月8日 上午11:12:49
 */

package com.mogujie.jarvis.core.expression;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class TestFixedDelayExpression {

    @Test
    public void testSecond() {
        ScheduleExpression exp1 = new FixedDelayExpression("s(5)");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getTimeBefore(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 8, 1, 1, 55));
        Assert.assertEquals(exp1.getTimeAfter(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 8, 1, 2, 5));

        Assert.assertTrue(!new FixedDelayExpression("s(-3)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("s(0)").isValid());

        Assert.assertTrue(!new FixedDelayExpression("s('2015-01-01 12:00:66',10)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("s('2015-01-01 12:00',10)").isValid());
    }

    @Test
    public void testMinute() {
        ScheduleExpression exp1 = new FixedDelayExpression("m(5)");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getTimeBefore(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 8, 0, 57, 0));
        Assert.assertEquals(exp1.getTimeAfter(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 8, 1, 7, 0));

        Assert.assertTrue(!new FixedDelayExpression("m(-3)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("m(0)").isValid());

        Assert.assertTrue(!new FixedDelayExpression("m('2015-01-01 12:00:66',10)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("m('2015-01-01 12:00',10)").isValid());
    }

    @Test
    public void testHour() {
        ScheduleExpression exp1 = new FixedDelayExpression("h(5)");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getTimeBefore(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 7, 20, 2, 0));
        Assert.assertEquals(exp1.getTimeAfter(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 8, 6, 2, 0));

        Assert.assertTrue(!new FixedDelayExpression("h(-3)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("h(0)").isValid());

        Assert.assertTrue(!new FixedDelayExpression("h('2015-01-01 12:00:66',10)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("h('2015-01-01 12:00',10)").isValid());
    }

    @Test
    public void testDay() {
        ScheduleExpression exp1 = new FixedDelayExpression("d(5)");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getTimeBefore(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 3, 1, 2, 0));
        Assert.assertEquals(exp1.getTimeAfter(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 13, 1, 2, 0));

        Assert.assertTrue(!new FixedDelayExpression("d(-3)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("d(0)").isValid());
    }

    @Test
    public void testMonth() {
        ScheduleExpression exp1 = new FixedDelayExpression("M(5)");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getTimeBefore(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 7, 8, 1, 2, 0));
        Assert.assertEquals(exp1.getTimeAfter(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2016, 5, 8, 1, 2, 0));

        Assert.assertTrue(!new FixedDelayExpression("M(-3)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("M(0)").isValid());

        Assert.assertTrue(!new FixedDelayExpression("M('2015-01-01 12:00:66',10)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("M('2015-01-01 12:00',10)").isValid());
    }

    @Test
    public void testYear() {
        ScheduleExpression exp1 = new FixedDelayExpression("y(5)");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getTimeBefore(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2010, 12, 8, 1, 2, 0));
        Assert.assertEquals(exp1.getTimeAfter(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2020, 12, 8, 1, 2, 0));

        Assert.assertTrue(!new FixedDelayExpression("y(-3)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("y(0)").isValid());

        Assert.assertTrue(!new FixedDelayExpression("y('2015-01-01 12:00:66',10)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("y('2015-01-01 12:00',10)").isValid());
    }

    @Test
    public void testWeek() {
        ScheduleExpression exp1 = new FixedDelayExpression("w(1)");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getTimeBefore(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 1, 1, 2, 0));
        Assert.assertEquals(exp1.getTimeAfter(new DateTime(2015, 12, 8, 1, 2, 0)), new DateTime(2015, 12, 15, 1, 2, 0));

        Assert.assertTrue(!new FixedDelayExpression("w(-3)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("w(0)").isValid());

        Assert.assertTrue(!new FixedDelayExpression("w('2015-01-01 12:00:66',10)").isValid());
        Assert.assertTrue(!new FixedDelayExpression("w('2015-01-01 12:00',10)").isValid());
    }
}
