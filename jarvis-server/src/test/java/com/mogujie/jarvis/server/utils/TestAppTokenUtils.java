/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月15日 下午3:39:02
 */

package com.mogujie.jarvis.server.utils;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.mogujie.jarvis.core.exception.AppTokenInvalidException;
import com.mogujie.jarvis.core.util.AppTokenUtils;

public class TestAppTokenUtils {

    private String appKey = UUID.randomUUID().toString().replace("-", "");

    @Test
    public void testValidToken() throws AppTokenInvalidException {
        String token = AppTokenUtils.generateToken(System.currentTimeMillis() / 1000, appKey);
        Assert.assertTrue(AppTokenUtils.verifyToken(appKey, token));
    }

    @Test(expected = AppTokenInvalidException.class)
    public void testInvalidToken() throws AppTokenInvalidException {
        String token = "invalid token";
        AppTokenUtils.verifyToken(appKey, token);
    }

    @Test(expected = AppTokenInvalidException.class)
    public void testExpiredToken() throws AppTokenInvalidException {
        long ts = System.currentTimeMillis() / 1000 - 2 * AppTokenUtils.MAX_TIMESTAMP_OFFSET;
        String token = AppTokenUtils.generateToken(ts, appKey);
        AppTokenUtils.verifyToken(appKey, token);
    }
}
