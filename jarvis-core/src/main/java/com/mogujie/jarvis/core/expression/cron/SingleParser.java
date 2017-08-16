/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年1月13日 下午1:57:53
 */
package com.mogujie.jarvis.core.expression.cron;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Range;

/**
 * @author wuya
 *
 */
public class SingleParser extends AbstractParser {

    private Set<Integer> set = new HashSet<>();
    private Set<Integer> result = new HashSet<>();
    private Range<Integer> range;
    private DurationField type;

    public SingleParser(Range<Integer> range, DurationField type) {
        super(range, type);
        this.range = range;
        this.type = type;
    }

    @Override
    public boolean matches(String cronFieldExp) throws ParseException {
        if (CharMatcher.DIGIT.matchesAllOf(cronFieldExp)) {
            int value = Integer.parseInt(cronFieldExp);
            if (range.contains(value)) {
                set.add(value);
                return true;
            } else {
                throw new ParseException(
                        String.format("Invalid value of %s: %s, out of range %s", type.name, cronFieldExp, range.toString().replace("‥", ", ")), -1);
            }
        }

        return false;
    }

    @Override
    public Set<Integer> parse(DateTime dateTime) {
        if (type.equals(DurationField.DAY_OF_WEEK)) {
            if (set != null) {
                result.clear();

                MutableDateTime mdt = dateTime.dayOfMonth().withMaximumValue().toMutableDateTime();
                int maxDayOfMonth = mdt.getDayOfMonth();
                for (int i = 1; i <= maxDayOfMonth; i++) {
                    mdt.setDayOfMonth(i);
                    if (set.contains(mdt.getDayOfWeek())) {
                        result.add(mdt.getDayOfMonth());
                    }
                }

                return result;
            }
        }

        return set;
    }
}
