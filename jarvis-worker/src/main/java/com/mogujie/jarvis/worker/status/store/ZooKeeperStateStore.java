/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月6日 下午4:58:37
 */

package com.mogujie.jarvis.worker.status.store;

import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.worker.status.TaskStateStore;
import com.mogujie.jarvis.worker.util.KryoUtils;

public class ZooKeeperStateStore implements TaskStateStore {

    private CuratorFramework client;

    private static final String ZK_TASK_PATH = "/task";
    private static final String ZK_STATUS_PATH = "/status";

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void init(Configuration conf) {
        String zookeeperQuorum = conf.getString("zookeeper.quorum");
        String zookeeperZnodeParent = conf.getString("zookeeper.znode.parent", "jarvis_store");
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(3000, 3);
        client = CuratorFrameworkFactory.builder().connectString(zookeeperQuorum).namespace(zookeeperZnodeParent).retryPolicy(retryPolicy).build();
        client.start();
    }

    @Override
    public void write(TaskDetail taskDetail, int status) {
        String path = "/" + taskDetail.getFullId();
        try {
            client.inTransaction().create().forPath(path).and().setData().forPath(path + ZK_TASK_PATH, KryoUtils.toBytes(taskDetail)).and().setData()
                    .forPath(path + ZK_STATUS_PATH, Ints.toByteArray(status)).and().commit();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    @Override
    public void delete(String fullId) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath("/" + fullId);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    @Override
    public Map<TaskDetail, Integer> restore() {
        Map<TaskDetail, Integer> map = Maps.newHashMap();
        try {
            List<String> children = client.getChildren().forPath("/");
            for (String child : children) {
                TaskDetail taskDetail = (TaskDetail) KryoUtils.toObject(client.getData().forPath("/" + child + ZK_TASK_PATH));
                int status = Ints.fromByteArray(client.getData().forPath("/" + child + ZK_STATUS_PATH));
                map.put(taskDetail, status);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return map;
    }

    @Override
    public void close() {
        if (client != null) {
            CloseableUtils.closeQuietly(client);
        }
    }

}
