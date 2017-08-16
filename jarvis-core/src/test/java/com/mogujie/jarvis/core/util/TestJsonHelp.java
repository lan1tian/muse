/*
 * 蘑菇街 Inc. Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming Create Date: 2015年9月17日 下午8:23:47
 */

package com.mogujie.jarvis.core.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.mogujie.jarvis.core.util.JsonHelper;

/**
 * @author guangming
 *
 */
public class TestJsonHelp {

    @Test
    public void testParseMap2JSON() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        String jsonStr = JsonHelper.toJson(map);
        Assert.assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}", jsonStr);
    }

    @Test
    public void testParseJSON2Map() {
        String jsonStr = "{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}";
        Map<String, Object> jsonMap = JsonHelper.fromJson2JobParams(jsonStr);
        Assert.assertEquals(3, jsonMap.size());
        Assert.assertEquals("value1", jsonMap.get("key1"));
        Assert.assertEquals("value2", jsonMap.get("key2"));
        Assert.assertEquals("value3", jsonMap.get("key3"));
    }
}
