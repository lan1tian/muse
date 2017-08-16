/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月8日 下午8:06:17
 */

package com.mogujie.jarvis.core.expression;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class TestISO8601Expression {

    @Test
    public void testSecond() {
        ScheduleExpression expression = new ISO8601Expression("R5/2015-12-08T13:00:00/PT1S");
        Assert.assertTrue(expression.isValid());
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 7, 1, 2, 3)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 0)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 0, 1));
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 6)), null);

        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 7, 1, 2, 3)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 0)), new DateTime(2015, 12, 8, 13, 0, 1));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 0, 3));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 5)), null);

        ScheduleExpression expression2 = new ISO8601Expression("R/2015-12-08T13:00:00/PT1S");
        Assert.assertTrue(expression2.isValid());
        Assert.assertEquals(expression2.getTimeAfter(new DateTime(2016, 12, 8, 13, 0, 5)), new DateTime(2016, 12, 8, 13, 0, 6));
    }

    @Test
    public void testMinute() {
        ScheduleExpression expression = new ISO8601Expression("R5/2015-12-08T13:00:00/PT2M");
        Assert.assertTrue(expression.isValid());
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 7, 1, 2, 3)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 0)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 4, 6)), new DateTime(2015, 12, 8, 13, 4, 0));

        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 7, 1, 2, 3)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 0)), new DateTime(2015, 12, 8, 13, 2, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 2, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 4, 6)), new DateTime(2015, 12, 8, 13, 6, 0));

        ScheduleExpression expression2 = new ISO8601Expression("R/2015-12-08T13:00:00/PT2M");
        Assert.assertTrue(expression2.isValid());
        Assert.assertEquals(expression2.getTimeAfter(new DateTime(2016, 12, 12, 13, 0, 5)), new DateTime(2016, 12, 12, 13, 2, 0));
    }

    @Test
    public void testHour() {
        ScheduleExpression expression = new ISO8601Expression("R5/2015-12-08T13:00:00/PT2H");
        Assert.assertTrue(expression.isValid());
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 7, 1, 2, 3)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 0)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 15, 4, 6)), new DateTime(2015, 12, 8, 15, 0, 0));

        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 7, 1, 2, 3)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 0)), new DateTime(2015, 12, 8, 15, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 15, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 15, 4, 6)), new DateTime(2015, 12, 8, 17, 0, 0));

        ScheduleExpression expression2 = new ISO8601Expression("R/2015-12-08T13:00:00/PT2H");
        Assert.assertTrue(expression2.isValid());
        Assert.assertEquals(expression2.getTimeAfter(new DateTime(2016, 12, 12, 15, 0, 5)), new DateTime(2016, 12, 12, 17, 0, 0));
    }

    @Test
    public void testDay() {
        ScheduleExpression expression = new ISO8601Expression("R5/2015-12-08T13:00:00/P2D");
        Assert.assertTrue(expression.isValid());
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 7, 1, 2, 3)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 0)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 12, 15, 4, 6)), new DateTime(2015, 12, 12, 13, 0, 0));

        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 7, 1, 2, 3)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 0)), new DateTime(2015, 12, 10, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 10, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 12, 15, 4, 6)), new DateTime(2015, 12, 14, 13, 0, 0));

        ScheduleExpression expression2 = new ISO8601Expression("R/2015-12-08T13:00:00/P2D");
        Assert.assertTrue(expression2.isValid());
        Assert.assertEquals(expression2.getTimeAfter(new DateTime(2016, 12, 20, 15, 0, 5)), new DateTime(2016, 12, 22, 13, 0, 0));
    }

    @Test
    public void testMonth() {
        ScheduleExpression expression = new ISO8601Expression("R5/2015-12-08T13:00:00/P2M");
        Assert.assertTrue(expression.isValid());
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 7, 1, 2, 3)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 0)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2016, 4, 12, 15, 4, 6)), new DateTime(2016, 4, 8, 13, 0, 0));

        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 7, 1, 2, 3)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 0)), new DateTime(2016, 2, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2016, 2, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2016, 4, 12, 15, 4, 6)), new DateTime(2016, 6, 8, 13, 0, 0));

        ScheduleExpression expression2 = new ISO8601Expression("R/2015-12-08T13:00:00/P2M");
        Assert.assertTrue(expression2.isValid());
        Assert.assertEquals(expression2.getTimeAfter(new DateTime(2017, 12, 20, 15, 0, 5)), new DateTime(2018, 2, 8, 13, 0, 0));
    }

    @Test
    public void testYear() {
        ScheduleExpression expression = new ISO8601Expression("R5/2015-12-08T13:00:00/P2Y");
        Assert.assertTrue(expression.isValid());
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 7, 1, 2, 3)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 0)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2018, 4, 12, 15, 4, 6)), new DateTime(2017, 12, 8, 13, 0, 0));

        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 7, 1, 2, 3)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 0)), new DateTime(2017, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2017, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2018, 4, 12, 15, 4, 6)), new DateTime(2019, 12, 8, 13, 0, 0));

        ScheduleExpression expression2 = new ISO8601Expression("R/2015-12-08T13:00:00/P2Y");
        Assert.assertTrue(expression2.isValid());
        Assert.assertEquals(expression2.getTimeAfter(new DateTime(2020, 12, 20, 15, 0, 5)), new DateTime(2021, 12, 8, 13, 0, 0));
    }

    @Test
    public void test1() {
        ScheduleExpression expression = new ISO8601Expression("R5/2015-12-08T13:00:00/P1Y2M3D");
        Assert.assertTrue(expression.isValid());
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 7, 1, 2, 3)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 0)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2018, 4, 12, 15, 4, 6)), new DateTime(2017, 2, 11, 13, 0, 0));

        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 7, 1, 2, 3)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 0)), new DateTime(2017, 2, 11, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2017, 2, 11, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2018, 4, 12, 15, 4, 6)), new DateTime(2018, 4, 14, 13, 0, 0));
    }

    @Test
    public void test2() {
        ScheduleExpression expression = new ISO8601Expression("R5/2015-12-08T13:00:00/PT1H2M3S");
        Assert.assertTrue(expression.isValid());
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 7, 1, 2, 3)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 0)), null);
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeBefore(new DateTime(2018, 4, 12, 15, 4, 6)), null);

        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 7, 1, 2, 3)), new DateTime(2015, 12, 8, 13, 0, 0));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 0)), new DateTime(2015, 12, 8, 14, 2, 3));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2015, 12, 8, 13, 0, 2)), new DateTime(2015, 12, 8, 14, 2, 3));
        Assert.assertEquals(expression.getTimeAfter(new DateTime(2018, 4, 12, 15, 4, 6)), null);
    }
}
