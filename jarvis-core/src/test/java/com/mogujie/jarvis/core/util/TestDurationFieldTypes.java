/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月15日 下午3:16:01
 */

package com.mogujie.jarvis.core.util;

import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class TestDurationFieldTypes {

    @Test
    public void testValueOf() {
        Assert.assertEquals(DurationFieldTypes.valueOf('s'), DurationFieldType.seconds());
        Assert.assertEquals(DurationFieldTypes.valueOf('m'), DurationFieldType.minutes());
        Assert.assertEquals(DurationFieldTypes.valueOf('h'), DurationFieldType.hours());
        Assert.assertEquals(DurationFieldTypes.valueOf('d'), DurationFieldType.days());
        Assert.assertEquals(DurationFieldTypes.valueOf('w'), DurationFieldType.weeks());
        Assert.assertEquals(DurationFieldTypes.valueOf('M'), DurationFieldType.months());
        Assert.assertEquals(DurationFieldTypes.valueOf('y'), DurationFieldType.years());
    }
}
