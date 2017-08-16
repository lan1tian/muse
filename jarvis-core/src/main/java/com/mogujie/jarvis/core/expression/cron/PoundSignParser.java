/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年1月13日 下午2:37:47
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
public class PoundSignParser extends AbstractParser {

    private Set<Integer> set = new HashSet<>();
    private Range<Integer> range;
    private DurationField type;

    public PoundSignParser(Range<Integer> range, DurationField type) {
        super(range, type);
        this.range = range;
        this.type = type;
    }

    @Override
    public boolean matches(String cronFieldExp) {
        if ("*".equals(cronFieldExp)) {
            if (!type.equals(DurationField.DAY_OF_WEEK)) {
                int start = range.lowerEndpoint();
                int end = range.upperEndpoint();
                for (int i = start; i < end + 1; i++) {
                    set.add(i);
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public Set<Integer> parse(DateTime dateTime) {
        if (type.equals(DurationField.DAY_OF_WEEK)) {
            MutableDateTime mdt = dateTime.dayOfMonth().withMaximumValue().toMutableDateTime();
            int maxDayOfMonth = mdt.getDayOfMonth();
            for (int i = 1; i <= maxDayOfMonth; i++) {
                set.add(i);
            }
        }

        return set;
    }

}
