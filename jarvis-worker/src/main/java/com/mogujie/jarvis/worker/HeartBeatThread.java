/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月1日 下午3:01:04
 */

package com.mogujie.jarvis.worker;

import java.util.concurrent.TimeoutException;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.protocol.HeartBeatProtos.HeartBeatRequest;
import com.mogujie.jarvis.protocol.HeartBeatProtos.HeartBeatResponse;
import com.mogujie.jarvis.protocol.WorkerProtos.ServerRegistryResponse;
import com.mogujie.jarvis.protocol.WorkerProtos.WorkerRegistryRequest;
import com.mogujie.jarvis.worker.util.FutureUtils;

import akka.actor.ActorSelection;

/**
 * @author wuya
 */
public class HeartBeatThread extends Thread {

    private ActorSelection heartBeatActor;
    private TaskPool taskPool = TaskPool.INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger("heartbeat");

    public HeartBeatThread(ActorSelection heartBeatActor) {
        this.heartBeatActor = heartBeatActor;
    }

    private void registerWorker() {
        Configuration workerConfig = ConfigUtils.getWorkerConfig();
        int workerGroupId = workerConfig.getInt(WorkerConfigKeys.WORKER_GROUP_ID, 0);
        String workerKey = workerConfig.getString(WorkerConfigKeys.WORKER_KEY);
        WorkerRegistryRequest request = WorkerRegistryRequest.newBuilder().setKey(workerKey).build();

        try {
            ServerRegistryResponse response = (ServerRegistryResponse) FutureUtils.awaitResult(heartBeatActor, request, 3);
            if (!response.getSuccess()) {
                LOGGER.error("Worker register failed with group.id={}, worker.key={}", workerGroupId, workerKey);
            } else {
                LOGGER.info("Worker register successful");
            }
        } catch (TimeoutException e) {
            LOGGER.error("Worker register timeout." + e.toString());
        } catch (Exception e) {
            LOGGER.error("Worker register failed.", e);
        }
    }

    @Override
    public void run() {
        int jobNum = taskPool.size();
        HeartBeatRequest request = HeartBeatRequest.newBuilder().setJobNum(jobNum).build();
        Configuration workerConfig = ConfigUtils.getWorkerConfig();
        String akkaPath = workerConfig.getString(WorkerConfigKeys.SERVER_AKKA_PATH,"");
        String address = akkaPath.substring(akkaPath.indexOf("@") + 1);

        try {
            HeartBeatResponse response = (HeartBeatResponse) FutureUtils.awaitResult(heartBeatActor, request, 30);
            if (!response.getSuccess()) {
                LOGGER.error("refused! heartbeat to server[{}] msg:{}", address,response.getMessage());
                registerWorker();
            }else{
                LOGGER.info("success! heartbeat to server[{}]", address);
            }
        } catch (TimeoutException e) {
            LOGGER.error("timeout! heartbeat to server[{}]", address);
        } catch (Exception e) {
            LOGGER.error("exception! heartbeat to server[{}]", address, e);
        }
    }
}
