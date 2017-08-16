/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月17日 上午10:30:12
 */
package com.mogujie.jarvis.core.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import com.google.common.base.Throwables;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.SocketException;
import java.net.UnknownHostException;

public class ConfigUtils {

    private static PropertiesConfiguration workerConfig=null;
    private static PropertiesConfiguration serverConfig=null;
    private static PropertiesConfiguration logstorageConfig=null;
    private static PropertiesConfiguration restConfig=null;
    protected static final Logger LOGGER = LogManager.getLogger();

    /**
     * 读取Server配置
     */
    public synchronized static Configuration getServerConfig() {
        return getPropertiesConfig(serverConfig,"server.properties");
    }

    /**
     * 读取worker配置
     */
    public synchronized static Configuration getWorkerConfig() {
        return getPropertiesConfig(workerConfig,"worker.properties");
    }

    /**
     * 读取logstorage配置
     */
    public synchronized static Configuration getLogstorageConfig() {
        return getPropertiesConfig(logstorageConfig,"logstorage.properties");
    }

    /**
     * 读取rest配置
     */
    public synchronized static Configuration getRestConfig() {
        return getPropertiesConfig(restConfig,"rest.properties");
    }

    /**
     * 读取属性文件配置
     */
    private static Configuration getPropertiesConfig(PropertiesConfiguration config, String fileName) {
        if (config == null) {
            try {
                config = new PropertiesConfiguration(fileName);
                config.setReloadingStrategy(new FileChangedReloadingStrategy());
            } catch (ConfigurationException e) {
                Throwables.propagate(e);
            }
        }
        return config;
    }

    /**
     * 获取Akka的配置
     *
     * @param fileName
     * @return
     */
    public static Config getAkkaConfig(String fileName) {
        String key = "akka.remote.netty.tcp.hostname";
        Config akkaConfig = ConfigFactory.load(fileName);
        if (akkaConfig.hasPath(key) && !akkaConfig.getString(key).isEmpty()) {
            LOGGER.info("akka akkaConfig=="+akkaConfig);
            return akkaConfig;
        } else {
            try {
                String ip = IPUtils.getIPV4Address();
                LOGGER.info("akka ip=="+ip);
                return ConfigFactory.parseString(key + "=" + ip).withFallback(akkaConfig);
            } catch (UnknownHostException | SocketException ex) {
                Throwables.propagate(ex);
                return null;
            }
        }
    }
}
