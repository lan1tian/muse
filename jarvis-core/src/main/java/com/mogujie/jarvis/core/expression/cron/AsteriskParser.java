/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年1月13日 下午2:37:47
 */
package com.mogujie.jarvis.core.expression.cron;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.google.common.collect.Range;

/**
 * @author wuya
 *
 */
public class AsteriskParser extends AbstractParser {

    private Set<int[]> set = new HashSet<>();
    private Set<Integer> resultSet = new HashSet<>();
    private Range<Integer> range;
    private DurationField type;
    private static final Pattern ASTERISK_PATTERN = Pattern.compile("(\\d+)#(\\d+)");

    public AsteriskParser(Range<Integer> range, DurationField type) {
        super(range, type);
        this.range = range;
        this.type = type;
    }

    @Override
    public boolean matches(String cronFieldExp) throws ParseException {
        Matcher m = ASTERISK_PATTERN.matcher(cronFieldExp);
        if (m.matches()) {
            int dayOfWeek = Integer.parseInt(m.group(1));
            int sequence = Integer.parseInt(m.group(2));
            if (range.contains(dayOfWeek) && Range.closed(1, 5).contains(sequence)) {
                int[] value = { dayOfWeek, sequence };
                set.add(value);
                return true;
            } else {
                throw new ParseException(String.format("Invalid value of %s: %s, out of range.", type.name, cronFieldExp), -1);
            }
        }

        return false;
    }

    @Override
    public Set<Integer> parse(DateTime dateTime) {
        MutableDateTime mdt = dateTime.dayOfMonth().withMaximumValue().toMutableDateTime();
        int maxDayOfMonth = mdt.getDayOfMonth();
        mdt.setDayOfMonth(1);
        int firstDayOfWeek = mdt.getDayOfWeek();

        if (set != null) {
            resultSet.clear();

            for (int[] value : set) {
                int dayOfWeek = value[0];
                int sequence = value[1];
                int expectDay = 0;
                if (dayOfWeek >= firstDayOfWeek) {
                    expectDay = dayOfWeek - firstDayOfWeek + 7 * (sequence - 1) + 1;
                } else {
                    expectDay = dayOfWeek - firstDayOfWeek + 7 * sequence + 1;
                }

                if (expectDay <= maxDayOfMonth) {
                    resultSet.add(expectDay);
                }
            }

            return resultSet;
        }

        return null;
    }
}
