/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年1月14日 下午11:06:53
 */
package com.mogujie.jarvis.core.expression;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import com.mogujie.jarvis.core.expression.CronExpression;

/**
 * @author wuya
 *
 */
public class TestCronExpression {

    @Test
    public void testMinute() {
        assertEquals(new CronExpression("0 3 * * * ? *").getTimeAfter(new DateTime(2015, 1, 14, 23, 1)), new DateTime(2015, 1, 14, 23, 3));
        assertEquals(new CronExpression("0 29 * * * ? *").getTimeAfter(new DateTime(2015, 1, 14, 22, 34)), new DateTime(2015, 1, 14, 23, 29));
        assertEquals(new CronExpression("0 29 * * * ? *").getTimeAfter(new DateTime(2015, 1, 14, 22, 29)), new DateTime(2015, 1, 14, 23, 29));
        assertEquals(new CronExpression("0 1 * * * ? *").getTimeAfter(new DateTime(2015, 1, 14, 22, 29)), new DateTime(2015, 1, 14, 23, 1));
        assertEquals(new CronExpression("0 59 * * * ? *").getTimeAfter(new DateTime(2015, 1, 14, 22, 29)), new DateTime(2015, 1, 14, 22, 59));
        assertEquals(new CronExpression("0 * * * * ? *").getTimeAfter(new DateTime(2015, 1, 14, 22, 29)), new DateTime(2015, 1, 14, 22, 30));
    }

    @Test
    public void checkAll() {
        assertEquals(new CronExpression("* * * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00, 01)), new DateTime(2012, 4, 10, 13, 00, 02));
        assertEquals(new CronExpression("* * * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 02)), new DateTime(2012, 4, 10, 13, 02, 01));
        assertEquals(new CronExpression("* * * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 59, 59)), new DateTime(2012, 4, 10, 14, 00));
    }

    @Test
    public void checkMinuteNumber() {
        assertEquals(new CronExpression("0 3 * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 01)), new DateTime(2012, 4, 10, 13, 03));
        assertEquals(new CronExpression("0 3 * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 03)), new DateTime(2012, 4, 10, 14, 03));
    }

    @Test
    public void checkMinuteIncrement() {
        assertEquals(new CronExpression("0 0/15 * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00)), new DateTime(2012, 4, 10, 13, 15));
        assertEquals(new CronExpression("0 0/15 * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 15)), new DateTime(2012, 4, 10, 13, 30));
        assertEquals(new CronExpression("0 0/15 * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 30)), new DateTime(2012, 4, 10, 13, 45));
        assertEquals(new CronExpression("0 0/15 * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 45)), new DateTime(2012, 4, 10, 14, 00));
    }

    @Test
    public void checkMinuteList() {
        assertEquals(new CronExpression("0, 7,19 * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00)), new DateTime(2012, 4, 10, 13, 07));
        assertEquals(new CronExpression("0, 7,19 * * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 07)), new DateTime(2012, 4, 10, 13, 19));
    }

    @Test
    public void checkHourNumber() {
        assertEquals(new CronExpression("0 * 3 * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 01)), new DateTime(2012, 4, 11, 03, 00));
        assertEquals(new CronExpression("0 * 3 * * ?").getTimeAfter(new DateTime(2012, 4, 11, 03, 00)), new DateTime(2012, 4, 11, 03, 01));
        assertEquals(new CronExpression("0 * 3 * * ?").getTimeAfter(new DateTime(2012, 4, 11, 03, 59)), new DateTime(2012, 4, 12, 03, 00));
    }

    @Test
    public void checkHourIncrement() {
        assertEquals(new CronExpression("0 * 0/15 * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00)), new DateTime(2012, 4, 10, 15, 00));
        assertEquals(new CronExpression("0 * 0/15 * * ?").getTimeAfter(new DateTime(2012, 4, 10, 15, 00)), new DateTime(2012, 4, 10, 15, 01));
        assertEquals(new CronExpression("0 * 0/15 * * ?").getTimeAfter(new DateTime(2012, 4, 10, 15, 59)), new DateTime(2012, 4, 11, 00, 00));
        assertEquals(new CronExpression("0 * 0/15 * * ?").getTimeAfter(new DateTime(2012, 4, 11, 00, 00)), new DateTime(2012, 4, 11, 00, 01));
        assertEquals(new CronExpression("0 * 0/15 * * ?").getTimeAfter(new DateTime(2012, 4, 11, 15, 00)), new DateTime(2012, 4, 11, 15, 01));
    }

    @Test
    public void checkHourList() {
        assertEquals(new CronExpression("0 * 7,19 * * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00)), new DateTime(2012, 4, 10, 19, 00));
        assertEquals(new CronExpression("0 * 7,19 * * ?").getTimeAfter(new DateTime(2012, 4, 10, 19, 00)), new DateTime(2012, 4, 10, 19, 01));
        assertEquals(new CronExpression("0 * 7,19 * * ?").getTimeAfter(new DateTime(2012, 4, 10, 19, 59)), new DateTime(2012, 4, 11, 07, 00));
    }

    @Test
    public void checkDayOfMonthNumber() {
        assertEquals(new CronExpression("0 * * 3 * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00)), new DateTime(2012, 5, 03, 00, 00));
        assertEquals(new CronExpression("0 * * 3 * ?").getTimeAfter(new DateTime(2012, 5, 03, 00, 00)), new DateTime(2012, 5, 03, 00, 01));
        assertEquals(new CronExpression("0 * * 3 * ?").getTimeAfter(new DateTime(2012, 5, 03, 00, 59)), new DateTime(2012, 5, 03, 01, 00));
        assertEquals(new CronExpression("0 * * 3 * ?").getTimeAfter(new DateTime(2012, 5, 03, 23, 59)), new DateTime(2012, 6, 03, 00, 00));
    }

    @Test
    public void checkDayOfMonthIncrement() {
        assertEquals(new CronExpression("0 0 0 1/15 * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00)), new DateTime(2012, 4, 16, 00, 00));
        assertEquals(new CronExpression("0 0 0 1/15 * ?").getTimeAfter(new DateTime(2012, 4, 16, 00, 00)), new DateTime(2012, 5, 01, 00, 00));
        assertEquals(new CronExpression("0 0 0 1/15 * ?").getTimeAfter(new DateTime(2012, 4, 30, 00, 00)), new DateTime(2012, 5, 01, 00, 00));
        assertEquals(new CronExpression("0 0 0 1/15 * ?").getTimeAfter(new DateTime(2012, 5, 01, 00, 00)), new DateTime(2012, 5, 16, 00, 00));
    }

    @Test
    public void checkDayOfMonthList() {
        assertEquals(new CronExpression("0 0 0 7,19 * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00)), new DateTime(2012, 4, 19, 00, 00));
        assertEquals(new CronExpression("0 0 0 7,19 * ?").getTimeAfter(new DateTime(2012, 4, 19, 00, 00)), new DateTime(2012, 5, 07, 00, 00));
        assertEquals(new CronExpression("0 0 0 7,19 * ?").getTimeAfter(new DateTime(2012, 5, 07, 00, 00)), new DateTime(2012, 5, 19, 00, 00));
        assertEquals(new CronExpression("0 0 0 7,19 * ?").getTimeAfter(new DateTime(2012, 5, 30, 00, 00)), new DateTime(2012, 6, 07, 00, 00));
    }

    @Test
    public void checkDayOfMonthLast() {
        assertEquals(new CronExpression("0 0 0 L * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00)), new DateTime(2012, 4, 30, 00, 00));
        assertEquals(new CronExpression("0 0 0 L * ?").getTimeAfter(new DateTime(2012, 2, 12, 00, 00)), new DateTime(2012, 2, 29, 00, 00));
    }

    @Test
    public void checkDayOfMonthNumberLastL() {
        assertEquals(new CronExpression("0 0 0 3L * ?").getTimeAfter(new DateTime(2012, 4, 10, 13, 00)), new DateTime(2012, 4, 30 - 2, 00, 00));
        assertEquals(new CronExpression("0 0 0 3L * ?").getTimeAfter(new DateTime(2012, 2, 12, 00, 00)), new DateTime(2012, 2, 29 - 2, 00, 00));
    }

    @Test
    public void checkDayOfMonthClosestWeekdayW() {
        // 9 - is weekday in may
        assertEquals(new CronExpression("0 0 0 9W * ?").getTimeAfter(new DateTime(2012, 5, 2, 00, 00)), new DateTime(2012, 5, 9, 00, 00));

        // 9 - is weekday in may
        assertEquals(new CronExpression("0 0 0 9W * ?").getTimeAfter(new DateTime(2012, 5, 8, 00, 00)), new DateTime(2012, 5, 9, 00, 00));

        // 9 - saturday, friday closest weekday in june
        assertEquals(new CronExpression("0 0 0 9W * ?").getTimeAfter(new DateTime(2012, 5, 9, 00, 00)), new DateTime(2012, 6, 8, 00, 00));

        // 9 - sunday, monday closest weekday in september
        assertEquals(new CronExpression("0 0 0 9W * ?").getTimeAfter(new DateTime(2012, 9, 1, 00, 00)), new DateTime(2012, 9, 10, 00, 00));
    }

    @Test
    public void checkDayOfMonthInvalidModifier() {
        assertTrue(!new CronExpression("0 0 0 9X * ?").isValid());
    }

    @Test
    public void checkDayOfMonthInvalidIncrementModifier() {
        assertTrue(!new CronExpression("0 0 0 9#2 * ?").isValid());
    }

    @Test
    public void checkMonthNumber() {
        assertEquals(new CronExpression("0 0 0 1 5 ?").getTimeAfter(new DateTime(2012, 2, 12, 00, 00)), new DateTime(2012, 5, 1, 00, 00));
    }

    @Test
    public void checkMonthIncrement() {
        assertEquals(new CronExpression("0 0 0 1 5/2 ?").getTimeAfter(new DateTime(2012, 2, 12, 00, 00)), new DateTime(2012, 5, 1, 00, 00));
        assertEquals(new CronExpression("0 0 0 1 5/2 ?").getTimeAfter(new DateTime(2012, 5, 1, 00, 00)), new DateTime(2012, 7, 1, 00, 00));
        assertEquals(new CronExpression("0 0 0 1 5/10 ?").getTimeAfter(new DateTime(2012, 5, 1, 00, 00)), new DateTime(2013, 5, 1, 00, 00));
    }

    @Test
    public void checkMonthList() {
        assertEquals(new CronExpression("0 0 0 1 3,7,12 ?").getTimeAfter(new DateTime(2012, 2, 12, 00, 00)), new DateTime(2012, 3, 1, 00, 00));
        assertEquals(new CronExpression("0 0 0 1 3,7,12 ?").getTimeAfter(new DateTime(2012, 3, 1, 00, 00)), new DateTime(2012, 7, 1, 00, 00));
        assertEquals(new CronExpression("0 0 0 1 3,7,12 ?").getTimeAfter(new DateTime(2012, 7, 1, 00, 00)), new DateTime(2012, 12, 1, 00, 00));
    }

    @Test
    public void checkMonthInvalidModifier() {
        assertTrue(!new CronExpression("0 0 1 ? ?").isValid());
    }

    @Test
    public void checkDayOfWeekNumber() {
        assertEquals(new CronExpression("0 0 0 ? * 3").getTimeAfter(new DateTime(2012, 4, 1, 00, 00)), new DateTime(2012, 4, 4, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 3").getTimeAfter(new DateTime(2012, 4, 4, 00, 00)), new DateTime(2012, 4, 11, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 3").getTimeAfter(new DateTime(2012, 4, 12, 00, 00)), new DateTime(2012, 4, 18, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 3").getTimeAfter(new DateTime(2012, 4, 18, 00, 00)), new DateTime(2012, 4, 25, 00, 00));
    }

    @Test
    public void checkDayOfWeekIncrement() {
        assertEquals(new CronExpression("0 0 0 ? * 3/2").getTimeAfter(new DateTime(2012, 4, 1, 00, 00)), new DateTime(2012, 4, 4, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 3/2").getTimeAfter(new DateTime(2012, 4, 4, 00, 00)), new DateTime(2012, 4, 6, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 3/2").getTimeAfter(new DateTime(2012, 4, 6, 00, 00)), new DateTime(2012, 4, 8, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 3/2").getTimeAfter(new DateTime(2012, 4, 8, 00, 00)), new DateTime(2012, 4, 11, 00, 00));
    }

    @Test
    public void checkDayOfWeekList() {
        assertEquals(new CronExpression("0 0 0 ? * 1,5,7").getTimeAfter(new DateTime(2012, 4, 1, 00, 00)), new DateTime(2012, 4, 2, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 1,5,7").getTimeAfter(new DateTime(2012, 4, 2, 00, 00)), new DateTime(2012, 4, 6, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 1,5,7").getTimeAfter(new DateTime(2012, 4, 6, 00, 00)), new DateTime(2012, 4, 8, 00, 00));
    }

    @Test
    public void checkDayOfWeekLastFridayInMonth() {
        assertEquals(new CronExpression("0 0 0 ? * 5L").getTimeAfter(new DateTime(2012, 4, 1, 00, 00)), new DateTime(2012, 4, 27, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 5L").getTimeAfter(new DateTime(2012, 4, 27, 00, 00)), new DateTime(2012, 5, 25, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 5L").getTimeAfter(new DateTime(2012, 2, 6, 00, 00)), new DateTime(2012, 2, 24, 00, 00));
    }

    @Test
    public void checkDayOfWeekInvalidModifier() {
        assertTrue(!new CronExpression("0 0 0 * * 5W").isValid());
    }

    public void checkDayOfWeekInvalidIncrementModifier() {
        assertTrue(new CronExpression("0 0 0 * * 5?3").isValid());
    }

    @Test
    public void checkDayOfWeekShallInterpret7asSunday() {
        assertEquals(new CronExpression("0 0 0 ? * 7").getTimeAfter(new DateTime(2012, 4, 1, 00, 00)), new DateTime(2012, 4, 8, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 7L").getTimeAfter(new DateTime(2012, 4, 1, 00, 00)), new DateTime(2012, 4, 29, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 7#2").getTimeAfter(new DateTime(2012, 4, 1, 00, 00)), new DateTime(2012, 4, 8, 00, 00));
    }

    @Test
    public void checkDayOfWeekNthFridayInMonth() {
        assertEquals(new CronExpression("0 0 0 ? * 5#3").getTimeAfter(new DateTime(2012, 4, 1, 00, 00)), new DateTime(2012, 4, 20, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 5#3").getTimeAfter(new DateTime(2012, 4, 20, 00, 00)), new DateTime(2012, 5, 18, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 7#1").getTimeAfter(new DateTime(2012, 3, 30, 00, 00)), new DateTime(2012, 4, 1, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 7#1").getTimeAfter(new DateTime(2012, 4, 1, 00, 00)), new DateTime(2012, 5, 6, 00, 00));
        assertEquals(new CronExpression("0 0 0 ? * 3#5").getTimeAfter(new DateTime(2012, 2, 6, 00, 00)), new DateTime(2012, 2, 29, 00, 00)); // leapday
    }

    @Test
    public void checkLeapDay() {
        assertEquals(new CronExpression("0 0 1 * * ?").getTimeAfter(new DateTime(2000, 2, 28, 23, 59)), new DateTime(2000, 2, 29, 01, 00));
        assertEquals(new CronExpression("0 0 1 * * ?").getTimeAfter(new DateTime(2016, 2, 29, 23, 59)), new DateTime(2016, 3, 01, 01, 00));
        assertEquals(new CronExpression("0 0 1 * * ?").getTimeAfter(new DateTime(2016, 2, 28, 23, 59)), new DateTime(2016, 2, 29, 01, 00));
        assertEquals(new CronExpression("0 0 1 * * ?").getTimeAfter(new DateTime(2017, 2, 28, 23, 59)), new DateTime(2017, 3, 01, 01, 00));

        assertEquals(new CronExpression("0 0 1 29 * ?").getTimeAfter(new DateTime(2015, 2, 28, 23, 59)), new DateTime(2015, 3, 29, 01, 00));
        assertEquals(new CronExpression("0 0 1 29 2 ?").getTimeAfter(new DateTime(2017, 2, 28, 23, 59)), new DateTime(2020, 2, 29, 01, 00));

        assertTrue(!new CronExpression("0 0 0 29 2 ? 2015").isValid());
        assertTrue(new CronExpression("0 0 0 29 2 ? 2016").isValid());
    }

    @Test
    public void shallNotNotSupportRollingPeriod() {
        assertTrue(!new CronExpression("* * 5-1 * * ?").isValid());
    }
}
