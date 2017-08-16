/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月9日 下午3:23:29
 */

package com.mogujie.jarvis.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.mogujie.jarvis.core.domain.IdType;

public class TestIdUtils {

    @Test
    public void testParse() {
        String fullId = "123_456_789";
        Assert.assertEquals(IdUtils.parse(fullId, IdType.JOB_ID), 123);
        Assert.assertEquals(IdUtils.parse(fullId, IdType.TASK_ID), 456);
        Assert.assertEquals(IdUtils.parse(fullId, IdType.ATTEMPT_ID), 789);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseException() {
        String fullId = "123_456";
        Assert.assertEquals(IdUtils.parse(fullId, IdType.JOB_ID), 123);
        Assert.assertEquals(IdUtils.parse(fullId, IdType.TASK_ID), 456);
        Assert.assertEquals(IdUtils.parse(fullId, IdType.ATTEMPT_ID), 789);
    }
}
