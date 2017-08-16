/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年3月17日 下午4:20:19
 */

package com.mogujie.jarvis.rest.vo;

import java.util.Map;

import jersey.repackaged.com.google.common.collect.Maps;

/**
 * @author guangming
 *
 */
public class AppCounterVo extends AbstractVo {
    private Map<Integer, Integer> appCounterMap = Maps.newHashMap();

    public void setCounter(int appId, int counter) {
        appCounterMap.put(appId, counter);
    }

    public int getCounter(int appId) {
        return appCounterMap.containsKey(appId) ? appCounterMap.get(appId) : 0;
    }
}
