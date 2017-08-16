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

import com.mogujie.jarvis.core.util.DurationFieldTypes;

/**
 * Provides a parser and evaluator for fixed delay expressions, such as "d(1)".
 * Expression format: "s|m|h|d|w|M|y(n)"
 */
public class FixedDelayExpression extends ScheduleExpression {

    private int isValid;
    private DurationFieldType durationFieldType;
    private int value;
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("([smhdwMy])\\((\\d+)\\)");

    public FixedDelayExpression(String expression) {
        super(expression);
    }

    private DateTime calculateDateTime(DateTime dateTime, int value) {
        return dateTime.withFieldAdded(durationFieldType, value);
    }

    @Override
    public boolean isValid() {
        Matcher m = EXPRESSION_PATTERN.matcher(expression);
        if (m.matches()) {
            durationFieldType = DurationFieldTypes.valueOf(m.group(1).charAt(0));
            value = Integer.parseInt(m.group(2));
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
            return calculateDateTime(dateTime, -value);
        }

        return null;
    }

    @Override
    public DateTime getTimeAfter(DateTime dateTime) {
        if (isValid > 0 || (isValid == 0 && isValid())) {
            return calculateDateTime(dateTime, value);
        }

        return null;
    }

    @Override
    public String toString() {
        return expression;
    }
}
