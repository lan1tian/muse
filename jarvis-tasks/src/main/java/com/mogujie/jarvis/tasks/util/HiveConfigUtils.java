/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月17日 下午1:07:46
 */

package com.mogujie.jarvis.tasks.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogujie.jarvis.tasks.domain.HiveTaskEntity;


/**
 * @author wuya
 */
public class HiveConfigUtils {

    private static XMLConfiguration config;
    private static Map<String, HiveTaskEntity> map = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();

    static {
        try {
            config = new XMLConfiguration("job-hive.xml");
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException e) {
            LOGGER.error("", e);
        }
    }

    public synchronized static HiveTaskEntity getHiveJobEntry(String name) {
        if(config == null){
            return  null;
        }
        map.clear();
        List<HierarchicalConfiguration> list = config.configurationsAt(".App");
        for (HierarchicalConfiguration conf : list) {
            String appName = conf.getString("[@name]");
            String user = conf.getString("[@user]");
            boolean isAdmin = conf.getInt("[@isAdmin]") == 1;
            int maxResultRows = conf.getInt("[@maxResultRows]");
            int maxMapperNum = conf.getInt("[@maxMapperNum]");
            HiveTaskEntity entity = new HiveTaskEntity(appName, user, isAdmin, maxResultRows, maxMapperNum);
            map.put(appName, entity);
        }

        return map.get(name);
    }

}
