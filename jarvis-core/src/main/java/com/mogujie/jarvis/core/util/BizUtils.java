/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年3月7日 上午11:48:39
 */

package com.mogujie.jarvis.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author guangming
 *
 */
public class BizUtils {
    public static final String SEPARATOR = ",";

    public static String getBizGroupStr(Collection<?> bizIds) {
        String bizGroups = StringUtils.join(bizIds, SEPARATOR);
        return SEPARATOR + bizGroups + SEPARATOR;
    }

    public static List<Integer> getBizIds(String bizGroupStr) {
        bizGroupStr = bizGroupStr.trim();
        List<Integer> bizIds = new ArrayList<Integer>();
        if (bizGroupStr.length() > 2) {
            String[] bizGroups = StringUtils.split(bizGroupStr.substring(1, bizGroupStr.length() - 1), SEPARATOR);
            for (String bizId : bizGroups) {
                bizIds.add(Integer.valueOf(bizId));
            }
        }
        return bizIds;
    }
}
