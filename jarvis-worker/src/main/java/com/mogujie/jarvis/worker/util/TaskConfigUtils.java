/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月7日 下午3:16:59
 */

package com.mogujie.jarvis.worker.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.mogujie.jarvis.core.util.ReflectionUtils;
import com.mogujie.jarvis.worker.domain.TaskEntry;
import com.mogujie.jarvis.worker.strategy.AcceptanceStrategy;

public class TaskConfigUtils {

    private static XMLConfiguration config;

    static {
        try {
            config = new XMLConfiguration("task.xml");
        } catch (ConfigurationException e) {
            Throwables.propagate(e);
        }
    }

    public static Set<String> getTaskStrategies() {
        Set<String> set = new HashSet<>();
        List<Object> lists = config.configurationAt(".strategies").getList("strategy");
        for (Object object : lists) {
            set.add(object.toString());
        }
        return set;
    }

    public static Map<String, TaskEntry> getRegisteredTasks() {
        Map<String, TaskEntry> result = Maps.newHashMap();
        try {
            Set<String> commonStrategyNames = getTaskStrategies();
            List<AcceptanceStrategy> commonAcceptStrategies = new ArrayList<>();
            for (String commonStrategyName : commonStrategyNames) {
                AcceptanceStrategy acceptStrategy = ReflectionUtils.getInstanceByClassName(commonStrategyName);
                commonAcceptStrategies.add(acceptStrategy);
            }

            List<HierarchicalConfiguration> tasks = config.configurationsAt(".tasks.task");
            for (HierarchicalConfiguration taskConf : tasks) {
                String type = taskConf.getString("[@type]");
                String clazz = taskConf.getString("[@class]");
                String taskStatusLookupClass = taskConf.getString("lookup[@class]");

                List<AcceptanceStrategy> acceptStrategies = new ArrayList<AcceptanceStrategy>(commonAcceptStrategies);
                List<HierarchicalConfiguration> strategies = taskConf.configurationsAt(".strategies.strategy");
                for (HierarchicalConfiguration strategyConf : strategies) {
                    String strategyName = strategyConf.getRootNode().getValue().toString();
                    if (!commonStrategyNames.contains(strategyName)) {
                        AcceptanceStrategy acceptStrategy = ReflectionUtils.getInstanceByClassName(strategyName);
                        acceptStrategies.add(acceptStrategy);
                    }
                }

                TaskEntry taskEntry = new TaskEntry(type, clazz, acceptStrategies, taskStatusLookupClass);
                result.put(type, taskEntry);
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }

        return result;
    }
}
