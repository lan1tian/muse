/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年10月16日 上午10:27:05
 */

package com.mogujie.jarvis.core.util;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;
import com.mogujie.jarvis.core.exception.AppTokenInvalidException;

public class AppTokenUtils {

    private static final int APP_TOKEN_LENGTH = 42;
    public static final int MAX_TIMESTAMP_OFFSET = 180;

    public static String generateToken(long timestamp, String appKey) {
        return timestamp + Hashing.md5().hashString(appKey + timestamp, StandardCharsets.UTF_8).toString();
    }

    public static boolean verifyToken(String appKey, String token) throws AppTokenInvalidException {
        if (token == null || token.length() != APP_TOKEN_LENGTH) {
            throw new AppTokenInvalidException("Invalid appToken: " + token);
        }

        try {
            long timestamp = Long.parseLong(token.substring(0, 10));
            if (Math.abs(System.currentTimeMillis() / 1000 - timestamp) > MAX_TIMESTAMP_OFFSET) {
                throw new AppTokenInvalidException("Expired appToken: " + token);
            }

            String vaildTopken = generateToken(timestamp, appKey);
            if (vaildTopken.equals(token)) {
                return true;
            } else {
                throw new AppTokenInvalidException("AppToken auth faild: " + token);
            }
        } catch (NumberFormatException e) {
            throw new AppTokenInvalidException("Invalid appToken: " + token);
        }
    }
}
