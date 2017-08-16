/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年10月29日 下午10:41:44
 */

package com.mogujie.jarvis.core.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;

import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.util.DurationFieldTypes;

/**
 * Provides a parser and evaluator for fixed rate expressions, such as "d(1)".
 * Expression format: "s|m|h|d|w|M|y(n)"
 */
public class FixedRateExpression extends ScheduleExpression {

    private int isValid;
    private DurationFieldType durationFieldType;
    private int value;
    private DateTime firstDateTime;
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("([smhdwMy])\\(('(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})',)?(\\d+)\\)");

    public FixedRateExpression(String expression) {
        super(expression);
    }

    private DateTime calculateDateTime(DateTime dateTime, int value) {
        if (firstDateTime == null) {
            return dateTime.withFieldAdded(durationFieldType, value);
        } else {
            int difference = durationFieldType.getField(ISOChronology.getInstanceUTC()).getDifference(dateTime.getMillis(),
                    firstDateTime.getMillis());
            if (value > 0) {
                return firstDateTime.withFieldAdded(durationFieldType, (difference / value + 1) * value);
            } else {
                return firstDateTime.withFieldAdded(durationFieldType, difference / value * value);
            }
        }
    }

    @Override
    public boolean isValid() {
        Matcher m = EXPRESSION_PATTERN.matcher(expression);
        if (m.matches()) {
            durationFieldType = DurationFieldTypes.valueOf(m.group(1).charAt(0));
            String strDateTime = m.group(3);
            if (strDateTime != null) {
                try {
                    firstDateTime = DateTimeFormat.forPattern(JarvisConstants.DEFAULT_DATE_TIME_FORMAT).parseDateTime(strDateTime);
                } catch (IllegalFieldValueException e) {
                    isValid = -1;
                    return false;
                }
            }

            value = Integer.parseInt(m.group(4));
            if (value <= 0) {
                isValid = -1;
                return false;
            }

            isValid = 1;
            return true;
        }

        isValid = -1;
        return false;
    }

    @Override
    public DateTime getTimeBefore(DateTime dateTime) {
        if (isValid > 0 || (isValid == 0 && isValid())) {
            if (firstDateTime != null && !dateTime.isAfter(firstDateTime)) {
                return null;
            } else {
                return calculateDateTime(dateTime, -value);
            }
        }

        return null;
    }

    @Override
    public DateTime getTimeAfter(DateTime dateTime) {
        if (isValid > 0 || (isValid == 0 && isValid())) {
            if (firstDateTime != null && dateTime.isBefore(firstDateTime)) {
                return firstDateTime;
            } else {
                return calculateDateTime(dateTime, value);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return expression;
    }
}
