/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午7:57:49
 */

package com.mogujie.jarvis.worker.strategy.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.exception.AcceptanceException;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.worker.WorkerConfigKeys;
import com.mogujie.jarvis.worker.strategy.AcceptanceResult;
import com.mogujie.jarvis.worker.strategy.AcceptanceStrategy;

/**
 * @author wuya
 *
 */
public class YarnResourceAcceptanceStrategy implements AcceptanceStrategy {

    private int activeUriIndex = 0;
    private static final String DECIMAL_FORMAT = "#0.00";
    private static final Configuration CONFIG = ConfigUtils.getWorkerConfig();
    private static final double MAX_YARN_MEMORY_USAGE = CONFIG.getDouble(WorkerConfigKeys.YARN_MEMORY_USAGE_THRESHOLD, 0.9);
    private static final double MAX_YARN_VCORES_USAGE = CONFIG.getDouble(WorkerConfigKeys.YARN_VCORES_USAGE_THRESHOLD, 0.9);
    private static final List<Object> YARN_REST_API_URIS = CONFIG.getList(WorkerConfigKeys.YARN_RESOUCEMANAGER_REST_API_URIS);

    @Override
    public AcceptanceResult accept(TaskDetail taskDetail) throws AcceptanceException {
        Map<String, Object> parameters = taskDetail.getParameters();
        Object queueNameValue = parameters.get("queueName");
        String queueName = queueNameValue == null ? "root.default" : "root." + queueNameValue.toString();

        if (YARN_REST_API_URIS == null || YARN_REST_API_URIS.size() < 1) {
            throw new AcceptanceException("The value of " + WorkerConfigKeys.YARN_RESOUCEMANAGER_REST_API_URIS + " is invalid");
        }

        for (int i = 0, len = YARN_REST_API_URIS.size(); i < len; i++) {
            try {
                JsonNode json = Unirest.get(YARN_REST_API_URIS.get(activeUriIndex).toString()).asJson().getBody();
                JSONObject rootQueue = json.getObject().getJSONObject("scheduler").getJSONObject("schedulerInfo").getJSONObject("rootQueue");
                JSONObject clusterMaxResources = rootQueue.getJSONObject("maxResources");
                long clusterMaxMemory = clusterMaxResources.getLong("memory");
                long clusterMaxVCores = clusterMaxResources.getLong("vCores");

                JSONObject clusterUsedResources = rootQueue.getJSONObject("usedResources");
                long clusterUsedMemory = clusterUsedResources.getLong("memory");
                long clusterUsedVCores = clusterUsedResources.getLong("vCores");

                double clusterMemoryUsage = (double) clusterUsedMemory / clusterMaxMemory;
                double clusterVCoresUsage = (double) clusterUsedVCores / clusterMaxVCores;

                JSONArray childQueues = rootQueue.getJSONArray("childQueues");
                for (int j = 0, jlen = childQueues.length(); j < jlen; j++) {
                    JSONObject queue = childQueues.getJSONObject(j);
                    if (queue.getString("queueName").equals(queueName)) {
                        JSONObject queueMinResources = queue.getJSONObject("minResources");
                        long queueMinMemory = queueMinResources.getLong("memory");
                        long queueMinVCores = queueMinResources.getLong("vCores");

                        JSONObject queueUsedResources = queue.getJSONObject("usedResources");
                        long queueUsedMemory = queueUsedResources.getLong("memory");
                        long queueUsedVCores = queueUsedResources.getLong("vCores");

                        boolean memoryAllowed = queueUsedMemory < queueMinMemory || clusterMemoryUsage < MAX_YARN_MEMORY_USAGE;
                        boolean vCoresAllowed = queueUsedVCores < queueMinVCores || clusterVCoresUsage < MAX_YARN_VCORES_USAGE;

                        boolean result = memoryAllowed && vCoresAllowed;
                        if (result) {
                            return new AcceptanceResult(true, "");
                        } else if (!memoryAllowed) {
                            return new AcceptanceResult(false, "Yarn集群当前内存使用率: " + new DecimalFormat(DECIMAL_FORMAT).format(clusterMemoryUsage)
                                    + ", 超过阈值: " + MAX_YARN_MEMORY_USAGE);
                        } else {
                            return new AcceptanceResult(false, "Yarn集群当前虚拟内核使用率: " + new DecimalFormat(DECIMAL_FORMAT).format(clusterVCoresUsage)
                                    + ", 超过阈值: " + MAX_YARN_VCORES_USAGE);
                        }
                    }
                }
            } catch (UnirestException | JSONException e) {
                activeUriIndex = ++activeUriIndex % len;
            }
        }

        return new AcceptanceResult(true, "Can't get yarn resource metrics");
    }

}
