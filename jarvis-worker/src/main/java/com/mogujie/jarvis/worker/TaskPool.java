/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月1日 下午2:53:02
 */

package com.mogujie.jarvis.worker;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mogujie.jarvis.core.AbstractTask;

/**
 * @author wuya
 *
 */
public enum TaskPool {

    INSTANCE;

    private Map<String, AbstractTask> pool = Maps.newConcurrentMap();
    private Set<String> killTaskSet = Sets.newConcurrentHashSet();

    public void add(String fullId, AbstractTask task) {
        pool.put(fullId, task);
    }

    public void remove(String fullId) {
        pool.remove(fullId);
        killTaskSet.remove(fullId);
    }

    public AbstractTask get(String fullId) {
        return pool.get(fullId);
    }

    public void markTaskKilled(String fullId) {
        killTaskSet.add(fullId);
    }

    public boolean isKilled(String fullId) {
        return killTaskSet.contains(fullId);
    }

    public int size() {
        return pool.size();
    }
}