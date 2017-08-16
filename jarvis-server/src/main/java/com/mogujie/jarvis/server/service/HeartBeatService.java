/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月8日 下午1:52:05
 */

package com.mogujie.jarvis.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.inject.Singleton;
import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.server.guice.Injectors;

@Singleton
public class HeartBeatService {

    private static Set<WorkerInfo> offlineWorkers = Sets.newConcurrentHashSet();
    public static final int MAX_HEART_BEAT_TIMEOUT_SECONDS = 15;
    private static final Map<Integer, Cache<WorkerInfo, Integer>> HEART_BEAT_CACHE = Maps.newConcurrentMap();
    private static final Ordering<WorkerInfo> WORKER_ORDERING = new Ordering<WorkerInfo>() {

        @Override
        public int compare(WorkerInfo left, WorkerInfo right) {
            if (left == null) {
                return right == null ? 0 : -1;
            } else if (right == null) {
                return 1;
            }

            String leftPath = left.getAkkaRootPath();
            String rightPath = right.getAkkaRootPath();
            if (leftPath == null) {
                return rightPath == null ? 0 : -1;
            } else if (rightPath == null) {
                return 1;
            }

            return leftPath.compareTo(rightPath);
        }
    };

    static {
        WorkerService workerService = Injectors.getInjector().getInstance(WorkerService.class);
        offlineWorkers.addAll(workerService.getOffLineWorkers());
    }

    public void offlineWorker(WorkerInfo workerInfo) {
        offlineWorkers.add(workerInfo);
    }

    public void onlineWorker(WorkerInfo workerInfo) {
        offlineWorkers.remove(workerInfo);
    }

    public synchronized void put(int groupId, WorkerInfo workerInfo, final Integer jobNum) {
        Cache<WorkerInfo, Integer> cache = HEART_BEAT_CACHE.get(groupId);
        if (cache == null) {
            cache = CacheBuilder.newBuilder().expireAfterWrite(MAX_HEART_BEAT_TIMEOUT_SECONDS, TimeUnit.SECONDS).build();
            HEART_BEAT_CACHE.put(groupId, cache);
        }

        cache.put(workerInfo, jobNum);
    }

    public synchronized void remove(int groupId, WorkerInfo workerInfo) {
        Cache<WorkerInfo, Integer> cache = HEART_BEAT_CACHE.get(groupId);
        if (cache != null) {
            cache.invalidate(workerInfo);
            offlineWorker(workerInfo);
        }
    }

    public synchronized List<WorkerInfo> getWorkers(int groupId) {
        if (!HEART_BEAT_CACHE.containsKey(groupId)) {
            return Lists.newArrayList();
        }
        return WORKER_ORDERING.sortedCopy(HEART_BEAT_CACHE.get(groupId).asMap().keySet());
    }

    public synchronized List<WorkerInfo> getOnlineWorkers(int groupId) {
        List<WorkerInfo> result = getWorkers(groupId);
        for (int i = result.size() - 1; i >= 0; i--) {
            if (offlineWorkers.contains(result.get(i))) {
                result.remove(i);
            }
        }
        return result;
    }

    /**
     * 获取有心跳的worker
     */
    public Map<WorkerInfo, Integer> getLeavedWorkerInfo(int groupId) {

        if (groupId != 0) {
            return HEART_BEAT_CACHE.containsKey(groupId) ? HEART_BEAT_CACHE.get(groupId).asMap() : new HashMap<>();
        }

        Map<WorkerInfo, Integer> result = new HashMap<>();
        for (int key : HEART_BEAT_CACHE.keySet()) {
            result.putAll(HEART_BEAT_CACHE.get(key).asMap());
        }
        return result;
    }

}
