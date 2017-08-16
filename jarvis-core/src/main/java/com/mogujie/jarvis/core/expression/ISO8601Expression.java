/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月5日 下午7:56:32
 */

package com.mogujie.jarvis.core.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.ISOPeriodFormat;

/**
 * Provides a parser and evaluator for ISO8601 expressions, such as "R10/2015-12-28T15:52:00/PT2S".
 * See more: http://www.w3.org/TR/NOTE-datetime
 */
public class ISO8601Expression extends ScheduleExpression {

    private int isValid;
    private int repeat = -1;
    private DateTime startDateTime;
    private Period period;
    private static final Pattern ISO8601_PATTERN = Pattern.compile("(R\\d*)/(.*)/(P.+)");

    public ISO8601Expression(String expression) {
        super(expression);
    }

    @Override
    public boolean isValid() {
        Matcher m = ISO8601_PATTERN.matcher(expression);
        if (m.matches()) {
            String repeatStr = m.group(1);
            if (repeatStr.length() > 1) {
                repeat = Integer.parseInt(repeatStr.substring(1));
            }

            String startStr = m.group(2);
            if (startStr.isEmpty()) {
                startDateTime = DateTime.now(DateTimeZone.UTC);
            } else {
                try {
                    startDateTime = DateTime.parse(startStr);
                } catch (IllegalArgumentException e) {
                    isValid = -1;
                    return false;
                }
            }

            String periodStr = m.group(3);
            try {
                period = ISOPeriodFormat.standard().parsePeriod(periodStr);
            } catch (IllegalArgumentException e) {
                isValid = -1;
                return false;
            }

            isValid = 1;
            return true;
        } else {
            isValid = -1;
            return false;
        }
    }

    private DateTime calculateDateTime(DateTime dateTime, int value) {
        if (period.getYears() == 0 && period.getMonths() == 0) {
            Period diffPeriod = new Period(startDateTime, dateTime);
            if (diffPeriod.getYears() == 0 && diffPeriod.getMonths() == 0) {
                Seconds dateTimePeriod = diffPeriod.toStandardSeconds();
                int scalar = dateTimePeriod.getSeconds() / period.toStandardSeconds().getSeconds();
                if (repeat > 0 && Math.abs(scalar + value) >= repeat) {
                    return null;
                } else if (value > 0) {
                    return startDateTime.withPeriodAdded(period.toStandardSeconds(), scalar + 1);
                } else {
                    if (dateTimePeriod.getSeconds() % period.toStandardSeconds().getSeconds() == 0) {
                        return startDateTime.withPeriodAdded(period.toStandardSeconds(), scalar - 1);
                    } else {
                        return startDateTime.withPeriodAdded(period.toStandardSeconds(), scalar);
                    }
                }
            }
        }

        int scalar = 0;
        MutableDateTime result = startDateTime.toMutableDateTime();
        while (result.compareTo(dateTime) * (-Math.abs(value)) >= 0) {
            result.add(period, Math.abs(value));
            if (repeat > 0 && ++scalar >= repeat) {
                return null;
            }
        }

        if (value < 0) {
            result.add(period, value);
        }
        return result.toDateTime();

    }

    @Override
    public DateTime getTimeBefore(DateTime dateTime) {
        if (isValid > 0 || (isValid == 0 && isValid())) {
            if (!dateTime.isAfter(startDateTime)) {
                return null;
            } else {
                return calculateDateTime(dateTime, -1);
            }
        }

        return null;
    }

    @Override
    public DateTime getTimeAfter(DateTime dateTime) {
        if (isValid > 0 || (isValid == 0 && isValid())) {
            if (dateTime.isBefore(startDateTime)) {
                return startDateTime;
            } else {
                return calculateDateTime(dateTime, 1);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return expression;
    }

}
