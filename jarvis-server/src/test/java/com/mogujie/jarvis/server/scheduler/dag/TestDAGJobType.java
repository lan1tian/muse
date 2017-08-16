/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月29日 上午10:09:10
 */

package com.mogujie.jarvis.server.scheduler.dag;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author guangming
 *
 */
public class TestDAGJobType {
    private String s[] = {"---", "--t", "-d-", "-dt", "c--", "c-t" ,"cd-", "cdt"};
    private DAGJobType types[] = {DAGJobType.NONE, DAGJobType.TIME, DAGJobType.DEPEND,
            DAGJobType.DEPEND_TIME, DAGJobType.CYCLE, DAGJobType.CYCLE_TIME,
            DAGJobType.CYCLE_DEPEND, DAGJobType.ALL};

    @Test
    public void testGetInstanceByValue() {
        for (int i = 0; i < 7; i++) {
            DAGJobType type = DAGJobType.getInstance(s[i]);
            Assert.assertEquals(types[i], type);
        }
    }

    @Test
    public void testImplies() {
        Assert.assertTrue(DAGJobType.DEPEND_TIME.implies(DAGJobType.TIME));
        Assert.assertTrue(DAGJobType.DEPEND_TIME.implies(DAGJobType.DEPEND));
        Assert.assertFalse(DAGJobType.DEPEND_TIME.implies(DAGJobType.CYCLE));
        Assert.assertTrue(DAGJobType.CYCLE_DEPEND.implies(DAGJobType.CYCLE));
        Assert.assertTrue(DAGJobType.CYCLE_DEPEND.implies(DAGJobType.DEPEND));
        Assert.assertFalse(DAGJobType.CYCLE_DEPEND.implies(DAGJobType.TIME));
    }

    @Test
    public void testOperation() {
        // test or
        Assert.assertEquals(DAGJobType.DEPEND_TIME, DAGJobType.DEPEND.or(DAGJobType.TIME));
        Assert.assertEquals(DAGJobType.DEPEND_TIME, DAGJobType.DEPEND_TIME.or(DAGJobType.TIME));
        Assert.assertEquals(DAGJobType.DEPEND_TIME, DAGJobType.DEPEND_TIME.or(DAGJobType.DEPEND));
        Assert.assertEquals(DAGJobType.ALL, DAGJobType.DEPEND_TIME.or(DAGJobType.CYCLE_DEPEND));
        Assert.assertEquals(DAGJobType.ALL, DAGJobType.DEPEND_TIME.or(DAGJobType.CYCLE_TIME));
        // test and
        Assert.assertEquals(DAGJobType.NONE, DAGJobType.TIME.and(DAGJobType.DEPEND));
        Assert.assertEquals(DAGJobType.DEPEND, DAGJobType.DEPEND_TIME.and(DAGJobType.DEPEND));
        Assert.assertEquals(DAGJobType.TIME, DAGJobType.DEPEND_TIME.and(DAGJobType.TIME));
        Assert.assertEquals(DAGJobType.DEPEND, DAGJobType.DEPEND_TIME.and(DAGJobType.CYCLE_DEPEND));
        Assert.assertEquals(DAGJobType.TIME, DAGJobType.DEPEND_TIME.and(DAGJobType.CYCLE_TIME));
        // test remove
        Assert.assertEquals(DAGJobType.DEPEND, DAGJobType.DEPEND.remove(DAGJobType.TIME));
        Assert.assertEquals(DAGJobType.NONE, DAGJobType.DEPEND.remove(DAGJobType.DEPEND));
        Assert.assertEquals(DAGJobType.TIME, DAGJobType.DEPEND_TIME.remove(DAGJobType.DEPEND));
        Assert.assertEquals(DAGJobType.DEPEND, DAGJobType.DEPEND_TIME.remove(DAGJobType.TIME));
        Assert.assertEquals(DAGJobType.TIME, DAGJobType.DEPEND_TIME.remove(DAGJobType.CYCLE_DEPEND));
        Assert.assertEquals(DAGJobType.DEPEND, DAGJobType.DEPEND_TIME.remove(DAGJobType.CYCLE_TIME));
        Assert.assertEquals(DAGJobType.DEPEND, DAGJobType.ALL.remove(DAGJobType.CYCLE_TIME));
    }
}
