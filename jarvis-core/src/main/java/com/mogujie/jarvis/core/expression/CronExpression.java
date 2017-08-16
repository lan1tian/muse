/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年1月12日 下午10:37:42
 */
package com.mogujie.jarvis.core.expression;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.mogujie.jarvis.core.expression.cron.AbstractParser;
import com.mogujie.jarvis.core.expression.cron.AbstractParser.DurationField;
import com.mogujie.jarvis.core.expression.cron.AsteriskParser;
import com.mogujie.jarvis.core.expression.cron.LastDayOfMonthParser;
import com.mogujie.jarvis.core.expression.cron.MonthAbbreviationParser;
import com.mogujie.jarvis.core.expression.cron.NearestWeekdayOfMonthParser;
import com.mogujie.jarvis.core.expression.cron.PoundSignParser;
import com.mogujie.jarvis.core.expression.cron.RangeParser;
import com.mogujie.jarvis.core.expression.cron.SingleParser;
import com.mogujie.jarvis.core.expression.cron.StepParser;
import com.mogujie.jarvis.core.expression.cron.WeekAbbreviationParser;

/**
 * Provides a parser and evaluator for unix-like cron expressions, such as "0 0 12 * * ?".
 * Expression format: "seconds minutes hours dayOfMonth month dayOfWeek [years]"
 */
public class CronExpression extends ScheduleExpression {

    private List<AbstractParser> secondParsers;
    private List<AbstractParser> minuteParsers;
    private List<AbstractParser> hourParsers;
    private List<AbstractParser> dayOfMonthParsers;
    private List<AbstractParser> monthParsers;
    private List<AbstractParser> dayOfWeekParsers;
    private List<AbstractParser> yearParsers;

    private static final Range<Integer> SECOND_RANGE = Range.closed(0, 59);
    private static final Range<Integer> MINUTE_RANGE = Range.closed(0, 59);
    private static final Range<Integer> HOUR_RANGE = Range.closed(0, 23);
    private static final Range<Integer> DAY_OF_MONTH_RANGE = Range.closed(1, 31);
    private static final Range<Integer> MONTH_RANGE = Range.closed(1, 12);
    private static final Range<Integer> DAY_OF_WEEK_RANGE = Range.closed(1, 7);
    private static final Range<Integer> YEAR_RANGE = Range.closed(1970, 2099);

    public CronExpression(String expression) {
        super(expression);

        secondParsers = new ArrayList<>();
        secondParsers.add(new PoundSignParser(SECOND_RANGE, DurationField.SECOND));
        secondParsers.add(new RangeParser(SECOND_RANGE, DurationField.SECOND));
        secondParsers.add(new StepParser(SECOND_RANGE, DurationField.SECOND));
        secondParsers.add(new SingleParser(SECOND_RANGE, DurationField.SECOND));

        minuteParsers = new ArrayList<>();
        minuteParsers.add(new PoundSignParser(MINUTE_RANGE, DurationField.MINUTE));
        minuteParsers.add(new RangeParser(MINUTE_RANGE, DurationField.MINUTE));
        minuteParsers.add(new StepParser(MINUTE_RANGE, DurationField.MINUTE));
        minuteParsers.add(new SingleParser(MINUTE_RANGE, DurationField.MINUTE));

        hourParsers = new ArrayList<>();
        hourParsers.add(new PoundSignParser(HOUR_RANGE, DurationField.HOUR));
        hourParsers.add(new RangeParser(HOUR_RANGE, DurationField.HOUR));
        hourParsers.add(new StepParser(HOUR_RANGE, DurationField.HOUR));
        hourParsers.add(new SingleParser(HOUR_RANGE, DurationField.HOUR));

        dayOfMonthParsers = new ArrayList<>();
        dayOfMonthParsers.add(new PoundSignParser(DAY_OF_MONTH_RANGE, DurationField.DAY_OF_MONTH));
        dayOfMonthParsers.add(new RangeParser(DAY_OF_MONTH_RANGE, DurationField.DAY_OF_MONTH));
        dayOfMonthParsers.add(new StepParser(DAY_OF_MONTH_RANGE, DurationField.DAY_OF_MONTH));
        dayOfMonthParsers.add(new LastDayOfMonthParser(DAY_OF_MONTH_RANGE, DurationField.DAY_OF_MONTH));
        dayOfMonthParsers.add(new NearestWeekdayOfMonthParser(DAY_OF_MONTH_RANGE, DurationField.DAY_OF_MONTH));
        dayOfMonthParsers.add(new SingleParser(DAY_OF_MONTH_RANGE, DurationField.DAY_OF_MONTH));

        monthParsers = new ArrayList<>();
        monthParsers.add(new PoundSignParser(MONTH_RANGE, DurationField.MONTH));
        monthParsers.add(new RangeParser(MONTH_RANGE, DurationField.MONTH));
        monthParsers.add(new StepParser(MONTH_RANGE, DurationField.MONTH));
        monthParsers.add(new SingleParser(MONTH_RANGE, DurationField.MONTH));
        monthParsers.add(new MonthAbbreviationParser(MONTH_RANGE, DurationField.MONTH));

        dayOfWeekParsers = new ArrayList<>();
        dayOfWeekParsers.add(new PoundSignParser(DAY_OF_WEEK_RANGE, DurationField.DAY_OF_WEEK));
        dayOfWeekParsers.add(new RangeParser(DAY_OF_WEEK_RANGE, DurationField.DAY_OF_WEEK));
        dayOfWeekParsers.add(new StepParser(DAY_OF_WEEK_RANGE, DurationField.DAY_OF_WEEK));
        dayOfWeekParsers.add(new LastDayOfMonthParser(DAY_OF_WEEK_RANGE, DurationField.DAY_OF_WEEK));
        dayOfWeekParsers.add(new AsteriskParser(DAY_OF_WEEK_RANGE, DurationField.DAY_OF_WEEK));
        dayOfWeekParsers.add(new SingleParser(DAY_OF_WEEK_RANGE, DurationField.DAY_OF_WEEK));
        dayOfWeekParsers.add(new WeekAbbreviationParser(DAY_OF_WEEK_RANGE, DurationField.DAY_OF_WEEK));

        yearParsers = new ArrayList<>();
        yearParsers.add(new PoundSignParser(YEAR_RANGE, DurationField.YEAR));
        yearParsers.add(new RangeParser(YEAR_RANGE, DurationField.YEAR));
        yearParsers.add(new StepParser(YEAR_RANGE, DurationField.YEAR));
        yearParsers.add(new SingleParser(YEAR_RANGE, DurationField.YEAR));
    }

    private void validate(String[] exp) throws ParseException {
        if (exp.length != 7) {
            throw new ParseException("Unexpected end of expression.", -1);
        } else if ("?".equals(exp[DurationField.DAY_OF_MONTH.getIndex()]) && "?".equals(exp[DurationField.DAY_OF_WEEK.getIndex()])) {
            throw new ParseException("'?' can only be specfied for day-of-month or day-of-week.", -1);
        } else if (!"?".equals(exp[DurationField.DAY_OF_MONTH.getIndex()]) && !"?".equals(exp[DurationField.DAY_OF_WEEK.getIndex()])) {
            throw new ParseException("Support for specifying both a day-of-week and a day-of-month parameter is not implemented.", -1);
        } else if ("2".equals(exp[DurationField.MONTH.getIndex()]) && CharMatcher.DIGIT.matchesAllOf(exp[DurationField.DAY_OF_MONTH.getIndex()])) {
            int dayOfMonth = Integer.parseInt(exp[DurationField.DAY_OF_MONTH.getIndex()]);
            if (dayOfMonth > 29) {
                throw new ParseException("When month is 2, day-of-month should be in range [1, 29].", -1);
            }
        }
    }

    private String[] appendYearField(String[] exp) {
        if (exp.length == 6) {
            String[] newExp = new String[7];
            System.arraycopy(exp, 0, newExp, 0, exp.length);
            newExp[DurationField.YEAR.getIndex()] = "*";
            return newExp;
        }

        return exp;
    }

    private int searchNotLessThanIndex(List<Integer> sortedList, int value) {
        for (int i = 0, len = sortedList.size(); i < len; i++) {
            if (sortedList.get(i) >= value) {
                return i;
            }
        }

        return 0;
    }

    private int searchNotGreaterThanIndex(List<Integer> sortedList, int value) {
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            if (sortedList.get(i) <= value) {
                return i;
            }
        }

        return sortedList.size() - 1;
    }

    private List<Integer> parse(List<AbstractParser> pasers, String partCronExp, DateTime dateTime, DurationField type) throws ParseException {
        Set<Integer> result = new HashSet<>();
        for (String str : Splitter.on(",").omitEmptyStrings().split(partCronExp)) {
            boolean isMatch = false;
            for (AbstractParser paser : pasers) {
                if (paser.matches(str)) {
                    Set<Integer> value = paser.parse(dateTime);
                    if (value != null) {
                        result.addAll(value);
                    }
                    isMatch = true;
                    break;
                }
            }

            if (!isMatch) {
                throw new ParseException(String.format("Invalid value of %s: %s.", type.getName(), str), -1);
            }
        }

        return Ordering.natural().sortedCopy(result);
    }

    private List<Integer> parseDayValueList(String[] fixedCronExp, DateTime dateTime) throws ParseException {
        List<Integer> dayValues;
        if ("?".equals(fixedCronExp[DurationField.DAY_OF_MONTH.getIndex()])) {
            dayValues = parse(dayOfWeekParsers, fixedCronExp[DurationField.DAY_OF_WEEK.getIndex()], dateTime, DurationField.DAY_OF_WEEK);
        } else {
            dayValues = parse(dayOfMonthParsers, fixedCronExp[DurationField.DAY_OF_MONTH.getIndex()], dateTime, DurationField.DAY_OF_MONTH);
        }

        return dayValues;
    }

    @Override
    public DateTime getTimeAfter(DateTime dateTime) {
        try {
            String[] fixedCronExp = appendYearField(expression.split("\\s+"));
            validate(fixedCronExp);

            MutableDateTime mdt = dateTime.toMutableDateTime();
            mdt.setMillisOfSecond(0);

            List<Integer> secondValues = parse(secondParsers, fixedCronExp[DurationField.SECOND.getIndex()], dateTime, DurationField.SECOND);
            List<Integer> minuteValues = parse(minuteParsers, fixedCronExp[DurationField.MINUTE.getIndex()], dateTime, DurationField.MINUTE);
            List<Integer> hourValues = parse(hourParsers, fixedCronExp[DurationField.HOUR.getIndex()], dateTime, DurationField.HOUR);
            List<Integer> monthValues = parse(monthParsers, fixedCronExp[DurationField.MONTH.getIndex()], dateTime, DurationField.MONTH);
            List<Integer> yearValues = parse(yearParsers, fixedCronExp[DurationField.YEAR.getIndex()], dateTime, DurationField.YEAR);

            int yearStartIndex = searchNotLessThanIndex(yearValues, mdt.getYear());
            for (int yearIndex = yearStartIndex, yearLen = yearValues.size(); yearIndex < yearLen; yearIndex++) {
                int year = yearValues.get(yearIndex);
                mdt.setYear(year);
                int monthStartIndex = (year == dateTime.getYear()) ? searchNotLessThanIndex(monthValues, dateTime.getMonthOfYear()) : 0;

                for (int monthIndex = monthStartIndex, monthLen = monthValues.size(); monthIndex < monthLen; monthIndex++) {
                    int month = monthValues.get(monthIndex);
                    mdt.setMonthOfYear(month);
                    List<Integer> dayValues = parseDayValueList(fixedCronExp, mdt.toDateTime());
                    int dayStartIndex = (year == dateTime.getYear() && month == dateTime.getMonthOfYear())
                            ? searchNotLessThanIndex(dayValues, dateTime.getDayOfMonth()) : 0;

                    for (int dayIndex = dayStartIndex, dayLen = dayValues.size(); dayIndex < dayLen; dayIndex++) {
                        int day = dayValues.get(dayIndex);
                        int maxDayOfMonth = mdt.toDateTime().dayOfMonth().withMaximumValue().toLocalDate().getDayOfMonth();
                        if (day > maxDayOfMonth) {
                            break;
                        }
                        mdt.setDayOfMonth(day);
                        int hourStartIndex = (year == dateTime.getYear() && month == dateTime.getMonthOfYear() && day == dateTime.getDayOfMonth())
                                ? searchNotLessThanIndex(hourValues, dateTime.getHourOfDay()) : 0;

                        for (int hourIndex = hourStartIndex, hourLen = hourValues.size(); hourIndex < hourLen; hourIndex++) {
                            int hour = hourValues.get(hourIndex);
                            mdt.setHourOfDay(hour);
                            int minuteStartIndex = (year == dateTime.getYear() && month == dateTime.getMonthOfYear()
                                    && day == dateTime.getDayOfMonth() && hour == dateTime.getHourOfDay())
                                            ? searchNotLessThanIndex(minuteValues, dateTime.getMinuteOfHour()) : 0;

                            for (int minuteIndex = minuteStartIndex, minuteLen = minuteValues.size(); minuteIndex < minuteLen; minuteIndex++) {
                                int minute = minuteValues.get(minuteIndex);
                                int secondStartIndex = (year == dateTime.getYear() && month == dateTime.getMonthOfYear()
                                        && day == dateTime.getDayOfMonth() && hour == dateTime.getHourOfDay() && minute == dateTime.getMinuteOfHour())
                                                ? searchNotLessThanIndex(secondValues, dateTime.getSecondOfMinute()) : 0;
                                mdt.setMinuteOfHour(minute);
                                for (int secondIndex = secondStartIndex, secondLen = secondValues.size(); secondIndex < secondLen; secondIndex++) {
                                    int second = secondValues.get(secondIndex);
                                    mdt.setSecondOfMinute(second);
                                    if (mdt.isAfter(dateTime)) {
                                        return mdt.toDateTime();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            Throwables.propagate(e);
        }
        return null;
    }

    @Override
    public DateTime getTimeBefore(DateTime dateTime) {
        try {
            String[] fixedCronExp = appendYearField(expression.split("\\s+"));
            validate(fixedCronExp);

            MutableDateTime mdt = dateTime.toMutableDateTime();
            mdt.setMillisOfSecond(0);

            List<Integer> secondValues = parse(secondParsers, fixedCronExp[DurationField.SECOND.getIndex()], dateTime, DurationField.SECOND);
            List<Integer> minuteValues = parse(minuteParsers, fixedCronExp[DurationField.MINUTE.getIndex()], dateTime, DurationField.MINUTE);
            List<Integer> hourValues = parse(hourParsers, fixedCronExp[DurationField.HOUR.getIndex()], dateTime, DurationField.HOUR);
            List<Integer> monthValues = parse(monthParsers, fixedCronExp[DurationField.MONTH.getIndex()], dateTime, DurationField.MONTH);
            List<Integer> yearValues = parse(yearParsers, fixedCronExp[DurationField.YEAR.getIndex()], dateTime, DurationField.YEAR);

            int yearStartIndex = searchNotGreaterThanIndex(yearValues, mdt.getYear());
            for (int yearIndex = yearStartIndex; yearIndex >= 0; yearIndex--) {
                int year = yearValues.get(yearIndex);
                mdt.setYear(year);
                int monthStartIndex = (year == dateTime.getYear()) ? searchNotGreaterThanIndex(monthValues, dateTime.getMonthOfYear())
                        : monthValues.size() - 1;

                for (int monthIndex = monthStartIndex; monthIndex >= 0; monthIndex--) {
                    int month = monthValues.get(monthIndex);
                    mdt.setMonthOfYear(month);
                    List<Integer> dayValues = parseDayValueList(fixedCronExp, mdt.toDateTime());
                    int dayStartIndex = (year == dateTime.getYear() && month == dateTime.getMonthOfYear())
                            ? searchNotGreaterThanIndex(dayValues, dateTime.getDayOfMonth()) : dayValues.size() - 1;

                    for (int dayIndex = dayStartIndex; dayIndex >= 0; dayIndex--) {
                        int day = dayValues.get(dayIndex);
                        int maxDayOfMonth = mdt.toDateTime().dayOfMonth().withMaximumValue().toLocalDate().getDayOfMonth();
                        if (day > maxDayOfMonth) {
                            break;
                        }
                        mdt.setDayOfMonth(day);
                        int hourStartIndex = (year == dateTime.getYear() && month == dateTime.getMonthOfYear() && day == dateTime.getDayOfMonth())
                                ? searchNotGreaterThanIndex(hourValues, dateTime.getHourOfDay()) : hourValues.size() - 1;

                        for (int hourIndex = hourStartIndex; hourIndex >= 0; hourIndex--) {
                            int hour = hourValues.get(hourIndex);
                            mdt.setHourOfDay(hour);
                            int minuteStartIndex = (year == dateTime.getYear() && month == dateTime.getMonthOfYear()
                                    && day == dateTime.getDayOfMonth() && hour == dateTime.getHourOfDay())
                                            ? searchNotGreaterThanIndex(minuteValues, dateTime.getMinuteOfHour()) : minuteValues.size() - 1;

                            for (int minuteIndex = minuteStartIndex; minuteIndex >= 0; minuteIndex--) {
                                int minute = minuteValues.get(minuteIndex);
                                mdt.setMinuteOfHour(minute);
                                int secondStartIndex = (year == dateTime.getYear() && month == dateTime.getMonthOfYear()
                                        && day == dateTime.getDayOfMonth() && hour == dateTime.getHourOfDay() && minute == dateTime.getMinuteOfHour())
                                                ? searchNotGreaterThanIndex(secondValues, dateTime.getSecondOfMinute()) : secondValues.size() - 1;

                                for (int secondIndex = secondStartIndex; secondIndex >= 0; secondIndex--) {
                                    int second = secondValues.get(secondIndex);
                                    mdt.setSecondOfMinute(second);
                                    if (mdt.isBefore(dateTime)) {
                                        return mdt.toDateTime();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            Throwables.propagate(e);
        }

        return null;
    }

    public List<DateTime> getTimeAfter(DateTime dateTime, int n) throws ParseException {
        if (n < 1) {
            throw new IllegalArgumentException("n should be > 0, but given " + n);
        }

        List<DateTime> list = null;
        MutableDateTime mdt = dateTime.toMutableDateTime();
        for (int i = 0; i < n; i++) {
            DateTime value = getTimeAfter(mdt.toDateTime());
            if (value != null) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(value);
                mdt.setMillis(value.getMillis());
            } else {
                break;
            }
        }

        return list;
    }

    public List<DateTime> getTimeBefore(DateTime dateTime, int n) throws ParseException {
        if (n < 1) {
            throw new IllegalArgumentException("n should be > 0, but given " + n);
        }

        List<DateTime> list = null;
        MutableDateTime mdt = dateTime.toMutableDateTime();
        for (int i = 0; i < n; i++) {
            DateTime value = getTimeBefore(mdt.toDateTime());
            if (value != null) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(value);
                mdt.setMillis(value.getMillis());
            } else {
                break;
            }
        }

        return list;
    }

    @Override
    public boolean isValid() {
        DateTime dateTime = new DateTime(1970, 1, 1, 0, 0, 0);
        try {
            return getTimeAfter(dateTime) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String evaluate() {
        DateTime dateTime = new DateTime(1970, 1, 1, 0, 0, 0);
        try {
            getTimeAfter(dateTime);
        } catch (Exception e) {
            return e.getMessage().replace("java.text.ParseException: ", "");
        }

        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CronExpression other = (CronExpression) obj;
        return Objects.equals(expression, other.expression);
    }

    @Override
    public String toString() {
        return expression;
    }
}
