/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月9日 上午11:58:52
 */

package com.mogujie.jarvis.core.expression;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Range;

public class TestTimeOffsetExpression {

    @Test
    public void testMinute() {
        DateTime dateTime = new DateTime(2015, 12, 13, 12, 34, 56);

        DependencyExpression exp1 = new TimeOffsetExpression("cm");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 13, 12, 34, 0), new DateTime(2015, 12, 13, 12, 35, 0)));

        DependencyExpression exp2 = new TimeOffsetExpression("m(5)");
        Assert.assertTrue(exp2.isValid());
        Assert.assertEquals(exp2.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 13, 12, 29, 0), new DateTime(2015, 12, 13, 12, 34, 0)));

        DependencyExpression exp3 = new TimeOffsetExpression("m(5,10)");
        Assert.assertTrue(exp3.isValid());
        Assert.assertEquals(exp3.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 13, 12, 19, 0), new DateTime(2015, 12, 13, 12, 29, 0)));
    }

    @Test
    public void testHour() {
        DateTime dateTime = new DateTime(2015, 12, 13, 12, 34, 56);

        DependencyExpression exp1 = new TimeOffsetExpression("ch");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 13, 12, 0, 0), new DateTime(2015, 12, 13, 13, 0, 0)));

        DependencyExpression exp2 = new TimeOffsetExpression("h(5)");
        Assert.assertTrue(exp2.isValid());
        Assert.assertEquals(exp2.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 13, 7, 0, 0), new DateTime(2015, 12, 13, 12, 0, 0)));

        DependencyExpression exp3 = new TimeOffsetExpression("h(5,10)");
        Assert.assertTrue(exp3.isValid());
        Assert.assertEquals(exp3.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 12, 21, 0, 0), new DateTime(2015, 12, 13, 7, 0, 0)));
    }

    @Test
    public void testDay() {
        DateTime dateTime = new DateTime(2015, 12, 13, 12, 34, 56);

        DependencyExpression exp1 = new TimeOffsetExpression("cd");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 13, 0, 0, 0), new DateTime(2015, 12, 14, 0, 0, 0)));

        DependencyExpression exp2 = new TimeOffsetExpression("d(5)");
        Assert.assertTrue(exp2.isValid());
        Assert.assertEquals(exp2.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 8, 0, 0, 0), new DateTime(2015, 12, 13, 0, 0, 0)));

        DependencyExpression exp3 = new TimeOffsetExpression("d(5,10)");
        Assert.assertTrue(exp3.isValid());
        Assert.assertEquals(exp3.getRange(dateTime), Range.closedOpen(new DateTime(2015, 11, 28, 0, 0, 0), new DateTime(2015, 12, 8, 0, 0, 0)));
    }

    @Test
    public void testMonth() {
        DateTime dateTime = new DateTime(2015, 12, 13, 12, 34, 56);

        DependencyExpression exp1 = new TimeOffsetExpression("cM");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 1, 0, 0, 0), new DateTime(2016, 1, 1, 0, 0, 0)));

        DependencyExpression exp2 = new TimeOffsetExpression("M(5)");
        Assert.assertTrue(exp2.isValid());
        Assert.assertEquals(exp2.getRange(dateTime), Range.closedOpen(new DateTime(2015, 7, 1, 0, 0, 0), new DateTime(2015, 12, 1, 0, 0, 0)));

        DependencyExpression exp3 = new TimeOffsetExpression("M(5,10)");
        Assert.assertTrue(exp3.isValid());
        Assert.assertEquals(exp3.getRange(dateTime), Range.closedOpen(new DateTime(2014, 9, 1, 0, 0, 0), new DateTime(2015, 7, 1, 0, 0, 0)));
    }

    @Test
    public void testYear() {
        DateTime dateTime = new DateTime(2015, 12, 13, 12, 34, 56);

        DependencyExpression exp1 = new TimeOffsetExpression("cy");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getRange(dateTime), Range.closedOpen(new DateTime(2015, 1, 1, 0, 0, 0), new DateTime(2016, 1, 1, 0, 0, 0)));

        DependencyExpression exp2 = new TimeOffsetExpression("y(5)");
        Assert.assertTrue(exp2.isValid());
        Assert.assertEquals(exp2.getRange(dateTime), Range.closedOpen(new DateTime(2010, 1, 1, 0, 0, 0), new DateTime(2015, 1, 1, 0, 0, 0)));

        DependencyExpression exp3 = new TimeOffsetExpression("y(5,10)");
        Assert.assertTrue(exp3.isValid());
        Assert.assertEquals(exp3.getRange(dateTime), Range.closedOpen(new DateTime(2000, 1, 1, 0, 0, 0), new DateTime(2010, 1, 1, 0, 0, 0)));
    }

    @Test
    public void testWeek() {
        DateTime dateTime = new DateTime(2015, 12, 13, 12, 34, 56);

        DependencyExpression exp1 = new TimeOffsetExpression("cw");
        Assert.assertTrue(exp1.isValid());
        Assert.assertEquals(exp1.getRange(dateTime), Range.closedOpen(new DateTime(2015, 12, 6, 0, 0, 0), new DateTime(2015, 12, 13, 0, 0, 0)));

        DependencyExpression exp2 = new TimeOffsetExpression("w(5)");
        Assert.assertTrue(exp2.isValid());
        Assert.assertEquals(exp2.getRange(dateTime), Range.closedOpen(new DateTime(2015, 11, 1, 0, 0, 0), new DateTime(2015, 12, 6, 0, 0, 0)));

        DependencyExpression exp3 = new TimeOffsetExpression("w(5,10)");
        Assert.assertTrue(exp3.isValid());
        Assert.assertEquals(exp3.getRange(dateTime), Range.closedOpen(new DateTime(2015, 8, 23, 0, 0, 0), new DateTime(2015, 11, 1, 0, 0, 0)));
    }

    @Test
    public void testGetReverseRange() {
        DateTime dateTime = new DateTime(2015, 12, 13, 12, 34, 56);
        Assert.assertEquals(new TimeOffsetExpression("cm").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 12, 34, 0), new DateTime(2015, 12, 13, 12, 35, 0)));
        Assert.assertEquals(new TimeOffsetExpression("m(3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 12, 35, 0), new DateTime(2015, 12, 13, 12, 38, 0)));
        Assert.assertEquals(new TimeOffsetExpression("m(-3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 12, 32, 0), new DateTime(2015, 12, 13, 12, 35, 0)));
        Assert.assertEquals(new TimeOffsetExpression("m(2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 12, 37, 0), new DateTime(2015, 12, 13, 12, 44, 0)));
        Assert.assertEquals(new TimeOffsetExpression("m(-2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 12, 33, 0), new DateTime(2015, 12, 13, 12, 40, 0)));
        Assert.assertEquals(new TimeOffsetExpression("m(2,-7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 12, 30, 0), new DateTime(2015, 12, 13, 12, 37, 0)));

        Assert.assertEquals(new TimeOffsetExpression("ch").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 12, 0, 0), new DateTime(2015, 12, 13, 13, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("h(3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 13, 0, 0), new DateTime(2015, 12, 13, 16, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("h(-3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 10, 0, 0), new DateTime(2015, 12, 13, 13, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("h(2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 15, 0, 0), new DateTime(2015, 12, 13, 22, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("h(-2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 11, 0, 0), new DateTime(2015, 12, 13, 18, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("h(2,-7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 8, 0, 0), new DateTime(2015, 12, 13, 15, 0, 0)));

        Assert.assertEquals(new TimeOffsetExpression("cd").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 13, 0, 0, 0), new DateTime(2015, 12, 14, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("d(3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 14, 0, 0, 0), new DateTime(2015, 12, 17, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("d(-3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 11, 0, 0, 0), new DateTime(2015, 12, 14, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("d(2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 16, 0, 0, 0), new DateTime(2015, 12, 23, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("d(-2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 12, 0, 0, 0), new DateTime(2015, 12, 19, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("d(2,-7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 9, 0, 0, 0), new DateTime(2015, 12, 16, 0, 0, 0)));

        Assert.assertEquals(new TimeOffsetExpression("cM").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 1, 0, 0, 0), new DateTime(2016, 1, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("M(3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2016, 1, 1, 0, 0, 0), new DateTime(2016, 4, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("M(-3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 10, 1, 0, 0, 0), new DateTime(2016, 1, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("M(2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2016, 3, 1, 0, 0, 0), new DateTime(2016, 10, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("M(-2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 11, 1, 0, 0, 0), new DateTime(2016, 6, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("M(2,-7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 8, 1, 0, 0, 0), new DateTime(2016, 3, 1, 0, 0, 0)));

        Assert.assertEquals(new TimeOffsetExpression("cy").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 1, 1, 0, 0, 0), new DateTime(2016, 1, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("y(3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2016, 1, 1, 0, 0, 0), new DateTime(2019, 1, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("y(-3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2013, 1, 1, 0, 0, 0), new DateTime(2016, 1, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("y(2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2018, 1, 1, 0, 0, 0), new DateTime(2025, 1, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("y(-2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2014, 1, 1, 0, 0, 0), new DateTime(2021, 1, 1, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("y(2,-7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2011, 1, 1, 0, 0, 0), new DateTime(2018, 1, 1, 0, 0, 0)));

        Assert.assertEquals(new TimeOffsetExpression("cw").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 14, 0, 0, 0), new DateTime(2015, 12, 21, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("w(3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 21, 0, 0, 0), new DateTime(2016, 1, 11, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("w(-3)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 11, 30, 0, 0, 0), new DateTime(2015, 12, 21, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("w(2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2016, 1, 4, 0, 0, 0), new DateTime(2016, 2, 22, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("w(-2,7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 12, 7, 0, 0, 0), new DateTime(2016, 1, 25, 0, 0, 0)));
        Assert.assertEquals(new TimeOffsetExpression("w(2,-7)").getReverseRange(dateTime),
                Range.closedOpen(new DateTime(2015, 11, 16, 0, 0, 0), new DateTime(2016, 1, 4, 0, 0, 0)));
    }
}
