/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月15日 下午3:25:22
 */

package com.mogujie.jarvis.worker.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class TestKryoUtils {

    @Test
    public void testString() {
        String obj = "123456789abcdefgh";
        Assert.assertEquals(obj, KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }

    @Test
    public void testInt() {
        int obj = 123;
        Assert.assertEquals(obj, KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }

    @Test
    public void testLong() {
        long obj = 1234567890L;
        Assert.assertEquals(obj, KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }

    @Test
    public void testDouble() {
        double obj = 1234567890d;
        Assert.assertEquals(obj, KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }

    @Test
    public void testFloat() {
        float obj = 12345678f;
        Assert.assertEquals(obj, KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }

    @Test
    public void testMap() {
        Map<String, Integer> obj = Maps.newHashMap();
        obj.put("a", 1);
        obj.put("b", 2);
        Assert.assertEquals(obj, KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }

    @Test
    public void testList() {
        List<String> obj = Lists.newArrayList();
        obj.add("a");
        obj.add("b");
        obj.add("c");
        Assert.assertEquals(obj, KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }

    @Test
    public void testSet() {
        Set<String> obj = Sets.newHashSet();
        obj.add("a");
        obj.add("b");
        obj.add("c");
        Assert.assertEquals(obj, KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }

    @Test
    public void testArray() {
        int[] obj = new int[] { 1, 2, 3, 4, 5 };
        Assert.assertArrayEquals(obj, (int[]) KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }

    @Test
    public void testDateTime() {
        DateTime obj = DateTime.now();
        Assert.assertEquals(obj, KryoUtils.toObject(KryoUtils.toBytes(obj)));
    }
}
