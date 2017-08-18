/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月7日 上午10:13:42
 */

package com.mogujie.jarvis.worker;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Throwables;
import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.metrics.Metrics;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.core.util.ThreadUtils;
import com.mogujie.jarvis.protocol.WorkerProtos.ServerRegistryResponse;
import com.mogujie.jarvis.protocol.WorkerProtos.WorkerRegistryRequest;
import com.mogujie.jarvis.worker.actor.TaskActor;
import com.mogujie.jarvis.worker.util.FutureUtils;
import com.typesafe.config.Config;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.routing.SmallestMailboxPool;
import scala.concurrent.duration.Duration;

public class JarvisWorker {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        LOGGER.info("Starting jarvis worker...");

        try {

            Config akkaConfig = ConfigUtils.getAkkaConfig("akka-worker.conf");
            ActorSystem system = ActorSystem.create(JarvisConstants.WORKER_AKKA_SYSTEM_NAME, akkaConfig);

            Configuration workerConfig = ConfigUtils.getWorkerConfig();
//            serverAkkaPath==akka.tcp://server@172.17.7.230:10000/user/server
            String serverAkkaPath = workerConfig.getString(WorkerConfigKeys.SERVER_AKKA_PATH) + JarvisConstants.SERVER_AKKA_USER_PATH;

            int workerGroupId = workerConfig.getInt(WorkerConfigKeys.WORKER_GROUP_ID, 0);
            String workerKey = workerConfig.getString(WorkerConfigKeys.WORKER_KEY);

            // 注册Worker：注册失败时退出，注册超时一直重试
            boolean register = false;
            while (!register) {
                WorkerRegistryRequest request = WorkerRegistryRequest.newBuilder().setKey(workerKey).build();
//                serverActor==ActorSelection[Anchor(akka.tcp://server@172.17.7.230:10000/), Path(/user/server)]
                ActorSelection serverActor = system.actorSelection(serverAkkaPath);
                LOGGER.info("serverActor=="+serverActor);
                LOGGER.info("serverAkkaPath=="+serverAkkaPath);
                LOGGER.info("workerKey=="+workerKey);
                LOGGER.info("request=="+request);
                try {
                    ServerRegistryResponse response = (ServerRegistryResponse) FutureUtils.awaitResult(serverActor, request, 10);
                    if (!response.getSuccess()) {
                        LOGGER.error("Worker register failed with group.id={}, worker.key={}, exit", workerGroupId, workerKey);
                        system.terminate();
                        return;
                    } else {
                        LOGGER.info("Worker register successfully");
                        break;
                    }
                } catch (TimeoutException e) {
                    LOGGER.error("Worker register timeout, waiting to retry..." + e.toString());
                } catch (Exception e) {
                    LOGGER.error("Worker register failed, waiting to retry...", e);
                }

                ThreadUtils.sleep(workerConfig.getInt(WorkerConfigKeys.WORKER_REGISTRY_FAILED_INTERVAL, 5000));
            }

            // 心跳汇报
            ActorSelection heartBeatActor = system.actorSelection(serverAkkaPath);
            int heartBeatInterval = workerConfig.getInt(WorkerConfigKeys.WORKER_HEART_BEAT_INTERVAL_SECONDS, 10);
            system.scheduler().schedule(Duration.Zero(), Duration.create(heartBeatInterval, TimeUnit.SECONDS), new HeartBeatThread(heartBeatActor),
                    system.dispatcher());

            //与logStorage心跳
            String logStorageAkkaPath = workerConfig.getString(WorkerConfigKeys.LOGSTORAGE_AKKA_PATH) + JarvisConstants.LOGSTORAGE_AKKA_USER_PATH;
            ActorSelection logHeartbeatActor = system.actorSelection(logStorageAkkaPath);
            system.scheduler().schedule(Duration.Zero(), Duration.create(heartBeatInterval, TimeUnit.SECONDS),
                    new LogStorageHeartbeatThread(logHeartbeatActor), system.dispatcher());

            system.scheduler().schedule(Duration.Zero(), Duration.create(5, TimeUnit.SECONDS), TaskStatusRetryReporter.getInstance(),
                    system.dispatcher());

            int actorNum = workerConfig.getInt(WorkerConfigKeys.WORKER_ACTORS_NUM, 500);
            ActorRef taskActor = system.actorOf(new SmallestMailboxPool(actorNum).props(TaskActor.props()), JarvisConstants.WORKER_AKKA_SYSTEM_NAME);
            Thread TaskStateRestoreThread = new TaskStateRestoreThread(system, taskActor);
            TaskStateRestoreThread.start();
            LOGGER.info("TaskStateRestore started.");

            Metrics.start(ConfigUtils.getWorkerConfig());
            LOGGER.info("Metrics started.");

            LOGGER.info("Jarvis worker started.");

        } catch (Exception ex) {
            LOGGER.error("jarvis worker start error!", ex);
            Throwables.propagate(ex);
        }

    }

}
