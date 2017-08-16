/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年3月7日 下午1:14:40
 */

package com.mogujie.jarvis.core.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author guangming
 *
 */
public class TestBizUtils {
    @Test
    public void testId2Str() {
        List<Integer> ids = Lists.newArrayList(1, 2, 3);
        String str = BizUtils.getBizGroupStr(ids);
        Assert.assertEquals(",1,2,3,", str);

        ids = Lists.newArrayList(1);
        str = BizUtils.getBizGroupStr(ids);
        Assert.assertEquals(",1,", str);
    }

    @Test
    public void testStr2Ids() {
        String str = ",1,2,3,";
        List<Integer> ids = BizUtils.getBizIds(str);
        Assert.assertEquals("[1, 2, 3]", ids.toString());

        str = ",1,";
        ids = BizUtils.getBizIds(str);
        Assert.assertEquals("[1]", ids.toString());
    }
}
