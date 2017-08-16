/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月1日 下午2:30:09
 */

package com.mogujie.jarvis.core.expression;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;

import com.google.common.base.CharMatcher;
import com.google.common.collect.BoundType;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.util.DurationFieldTypes;

/**
 * Provides a parser and evaluator for timeOffset dependency expressions, such as "['yyyy-MM-dd 00:00:00',d(-1),d(1))".
 */
public class TimeOffsetExpression extends DependencyExpression {

    private int isValid;
    private char rangeStartFlag;
    private String format;
    private String startTimeOffset;
    private String endTimeOffset;
    private char rangeEndFlag;
    private String expressionFormula;

    private static final Map<Pattern, String> MAP = Maps.newHashMap();
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(
            "([\\(\\[])'([y\\d]{4}-[M\\d]{2}-[d\\d]{2} [H\\d]{2}:[m\\d]{2}:[s\\d]{2})',((([smhdwMy]\\(((-?\\d+)|[smhHdeMy])\\))+),)?(([smhdwMy]\\(((-?\\d+)|[smhHdeMy])\\))*)([\\)\\]])");
    private static final Pattern SINGLE_OFFSET_PATTERN = Pattern.compile("([smhdwMy])\\(((-?\\d+)|[smhHdeMy])\\)");

    static {
        MAP.put(Pattern.compile("cm"), "['yyyy-MM-dd HH:mm:00',m(-1),m(1))");
        MAP.put(Pattern.compile("m\\((-?\\d+)\\)"), "['yyyy-MM-dd HH:mm:00',m(a))");
        MAP.put(Pattern.compile("m\\((-?\\d+),(-?\\d+)\\)"), "['yyyy-MM-dd HH:mm:00',m(a),m(b))");

        MAP.put(Pattern.compile("ch"), "['yyyy-MM-dd HH:00:00',h(-1),h(1))");
        MAP.put(Pattern.compile("h\\((-?\\d+)\\)"), "['yyyy-MM-dd HH:00:00',h(a))");
        MAP.put(Pattern.compile("h\\((-?\\d+),(-?\\d+)\\)"), "['yyyy-MM-dd HH:00:00',h(a),h(b))");

        MAP.put(Pattern.compile("cd"), "['yyyy-MM-dd 00:00:00',d(-1),d(1))");
        MAP.put(Pattern.compile("d\\((-?\\d+)\\)"), "['yyyy-MM-dd 00:00:00',d(a))");
        MAP.put(Pattern.compile("d\\((-?\\d+),(-?\\d+)\\)"), "['yyyy-MM-dd 00:00:00',d(a),d(b))");

        MAP.put(Pattern.compile("cM"), "['yyyy-MM-01 00:00:00',M(-1),M(1))");
        MAP.put(Pattern.compile("M\\((-?\\d+)\\)"), "['yyyy-MM-01 00:00:00',M(a))");
        MAP.put(Pattern.compile("M\\((-?\\d+),(-?\\d+)\\)"), "['yyyy-MM-01 00:00:00',M(a),M(b))");

        MAP.put(Pattern.compile("cy"), "['yyyy-01-01 00:00:00',y(-1),y(1))");
        MAP.put(Pattern.compile("y\\((-?\\d+)\\)"), "['yyyy-01-01 00:00:00',y(a))");
        MAP.put(Pattern.compile("y\\((-?\\d+),(-?\\d+)\\)"), "['yyyy-01-01 00:00:00',y(a),y(b))");

        MAP.put(Pattern.compile("cw"), "['yyyy-MM-dd 00:00:00',d(e),w(-1))");
        MAP.put(Pattern.compile("w\\((-?\\d+)\\)"), "['yyyy-MM-dd 00:00:00',d(e),w(a))");
        MAP.put(Pattern.compile("w\\((-?\\d+),(-?\\d+)\\)"), "['yyyy-MM-dd 00:00:00',d(e)w(a),w(b))");
    }

    public TimeOffsetExpression(String expression) {
        super(expression);
    }

    public static String convertAbbrExp(String abbrExp) {
        for (Entry<Pattern, String> entry : MAP.entrySet()) {
            Pattern pattern = entry.getKey();
            Matcher m = pattern.matcher(abbrExp);
            if (m.matches()) {
                int groupCount = m.groupCount();
                switch (groupCount) {
                    case 0:
                        return entry.getValue();
                    case 1:
                        return entry.getValue().replace("a", m.group(1));
                    case 2:
                        return entry.getValue().replace("a", m.group(1)).replace("b", m.group(2));
                    default:
                        break;
                }
            }
        }

        return abbrExp;
    }

    private static MutableDateTime convertSingleTimeOffset(MutableDateTime mutableDateTime, String exp, OffsetType offsetType) {
        Matcher m = SINGLE_OFFSET_PATTERN.matcher(exp);
        if (m.matches()) {
            char unit = m.group(1).charAt(0);
            String strValue = m.group(2);
            int value = 0;
            if (CharMatcher.DIGIT.matchesAllOf(strValue)) {
                value = Integer.parseInt(strValue);
            } else {
                value = Integer.parseInt(mutableDateTime.toString(strValue));
            }

            if (offsetType == OffsetType.FRONT) {
                mutableDateTime.add(DurationFieldTypes.valueOf(unit), -value);
            } else {
                mutableDateTime.add(DurationFieldTypes.valueOf(unit), value);
            }
        }

        return mutableDateTime;
    }

    @Override
    public boolean isValid() {
        if (expression == null) {
            return false;
        }
        for (Entry<Pattern, String> entry : MAP.entrySet()) {
            Pattern pattern = entry.getKey();
            Matcher m = pattern.matcher(expression);
            if (m.matches()) {
                expressionFormula = convertAbbrExp(expression);
                break;
            }
        }
        if (expressionFormula == null) {
            return false;
        }
        Matcher m = EXPRESSION_PATTERN.matcher(expressionFormula);
        if (m.matches()) {

            rangeStartFlag = m.group(1).charAt(0);
            format = m.group(2);
            startTimeOffset = m.group(4);
            endTimeOffset = m.group(8);
            rangeEndFlag = m.group(12).charAt(0);

            try {
                DateTimeFormat.forPattern(JarvisConstants.DEFAULT_DATE_TIME_FORMAT).parseDateTime(DateTime.now().toString(format));
                isValid = 1;
                return true;
            } catch (IllegalFieldValueException e) {
                isValid = -1;
                return false;
            }
        } else {
            isValid = -1;
            return false;
        }
    }

    @Override
    public Range<DateTime> getRange(DateTime dateTime) {
        if (isValid > 0 || (isValid == 0 && isValid())) {
            DateTime currentDateTime = DateTimeFormat.forPattern(JarvisConstants.DEFAULT_DATE_TIME_FORMAT).parseDateTime(dateTime.toString(format));
            MutableDateTime startDateTime = currentDateTime.toMutableDateTime();
            if (startTimeOffset != null) {
                Matcher startMatcher = SINGLE_OFFSET_PATTERN.matcher(startTimeOffset);
                while (startMatcher.find()) {
                    startDateTime = convertSingleTimeOffset(startDateTime, startMatcher.group(), OffsetType.FRONT);
                }
            }

            MutableDateTime endDateTime = new MutableDateTime(startDateTime);
            Matcher endMatcher = SINGLE_OFFSET_PATTERN.matcher(endTimeOffset);
            while (endMatcher.find()) {
                endDateTime = convertSingleTimeOffset(endDateTime, endMatcher.group(), OffsetType.FRONT);
            }

            DateTime start = startDateTime.isBefore(endDateTime) ? startDateTime.toDateTime() : endDateTime.toDateTime();
            DateTime end = startDateTime.isAfter(endDateTime) ? startDateTime.toDateTime() : endDateTime.toDateTime();

            BoundType lowerBoundType = rangeStartFlag == '(' ? BoundType.OPEN : BoundType.CLOSED;
            BoundType upperBoundType = rangeEndFlag == ')' ? BoundType.OPEN : BoundType.CLOSED;
            return Range.range(start, lowerBoundType, end, upperBoundType);
        }

        return null;
    }

    private Range<DateTime> fixRange(Range<DateTime> range) {
        DateTime lowerEndpoint = range.lowerEndpoint();
        DateTime upperEndpoint = range.upperEndpoint();

        BoundType lowerBoundType = range.lowerBoundType();
        BoundType upperBoundType = range.upperBoundType();
        switch (format) {
            case "yyyy-MM-dd HH:mm:00":
                return Range.range(lowerEndpoint.plusMinutes(1), lowerBoundType, upperEndpoint.plusMinutes(1), upperBoundType);
            case "yyyy-MM-dd HH:00:00":
                return Range.range(lowerEndpoint.plusHours(1), lowerBoundType, upperEndpoint.plusHours(1), upperBoundType);
            case "yyyy-MM-dd 00:00:00":
                return Range.range(lowerEndpoint.plusDays(1), lowerBoundType, upperEndpoint.plusDays(1), upperBoundType);
            case "yyyy-MM-01 00:00:00":
                return Range.range(lowerEndpoint.plusMonths(1), lowerBoundType, upperEndpoint.plusMonths(1), upperBoundType);
            case "yyyy-01-01 00:00:00":
                return Range.range(lowerEndpoint.plusYears(1), lowerBoundType, upperEndpoint.plusYears(1), upperBoundType);
            default:
                return range;
        }
    }

    @Override
    public Range<DateTime> getReverseRange(DateTime dateTime) {
        Range<DateTime> range = getRange(dateTime);
        if (range == null) {
            return null;
        }

        DateTime formatedDateTime = DateTimeFormat.forPattern(JarvisConstants.DEFAULT_DATE_TIME_FORMAT).parseDateTime(dateTime.toString(format));
        DateTime lowerEndpoint = null;
        DateTime upperEndpoint = null;

        switch (format) {
            case "yyyy-MM-01 00:00:00":
                lowerEndpoint = formatedDateTime.withPeriodAdded(Months.monthsBetween(range.upperEndpoint(), formatedDateTime), 1);
                upperEndpoint = formatedDateTime.withPeriodAdded(Months.monthsBetween(formatedDateTime, range.lowerEndpoint()), -1);
                break;
            case "yyyy-01-01 00:00:00":
                lowerEndpoint = formatedDateTime.withPeriodAdded(Years.yearsBetween(range.upperEndpoint(), formatedDateTime), 1);
                upperEndpoint = formatedDateTime.withPeriodAdded(Years.yearsBetween(formatedDateTime, range.lowerEndpoint()), -1);
                break;
            default:
                long t1 = formatedDateTime.getMillis() - range.lowerEndpoint().getMillis();
                long t2 = range.upperEndpoint().getMillis() - formatedDateTime.getMillis();
                lowerEndpoint = formatedDateTime.minus(t2);
                upperEndpoint = formatedDateTime.plus(t1);
                break;
        }

        if (lowerEndpoint.isAfter(upperEndpoint)) {
            return fixRange(Range.range(upperEndpoint, range.lowerBoundType(), lowerEndpoint, range.upperBoundType()));
        }

        return fixRange(Range.range(lowerEndpoint, range.lowerBoundType(), upperEndpoint, range.upperBoundType()));
    }

    @Override
    public String toString() {
        return expression;
    }

    public String getExpressionFormula() {
        return expressionFormula;
    }

}
