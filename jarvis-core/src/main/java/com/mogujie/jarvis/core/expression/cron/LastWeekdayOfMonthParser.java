/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年1月13日 下午1:57:53
 */
package com.mogujie.jarvis.core.expression.cron;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.google.common.collect.Range;

/**
 * @author wuya
 *
 */
public class LastWeekdayOfMonthParser extends AbstractParser {

    private Set<Integer> set = new HashSet<>();

    public LastWeekdayOfMonthParser(Range<Integer> range, DurationField type) {
        super(range, type);
    }

    @Override
    public boolean matches(String cronFieldExp) {
        return "LW".equals(cronFieldExp);
    }

    @Override
    public Set<Integer> parse(DateTime dateTime) {
        MutableDateTime mdt = dateTime.dayOfMonth().withMaximumValue().toMutableDateTime();
        while (mdt.getDayOfWeek() > 5) {
            mdt.addDays(-1);
        }

        set.add(mdt.getDayOfMonth());
        return set;
    }

}
