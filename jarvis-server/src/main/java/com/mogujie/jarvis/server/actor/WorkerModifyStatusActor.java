/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月22日 下午3:25:50
 */

package com.mogujie.jarvis.server.actor;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.core.domain.WorkerStatus;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.protocol.WorkerProtos.RestServerModifyWorkerStatusRequest;
import com.mogujie.jarvis.protocol.WorkerProtos.ServerModifyWorkerStatusResponse;
import com.mogujie.jarvis.server.WorkerRegistry;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.HeartBeatService;
import com.mogujie.jarvis.server.service.WorkerService;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class WorkerModifyStatusActor extends UntypedActor {

    private WorkerService workerService = Injectors.getInjector().getInstance(WorkerService.class);
    private static final Logger LOGGER = LogManager.getLogger();

    public static Props props() {
        return Props.create(WorkerModifyStatusActor.class);
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof RestServerModifyWorkerStatusRequest) {
            updateStatus((RestServerModifyWorkerStatusRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    public void updateStatus(RestServerModifyWorkerStatusRequest request) {
        ServerModifyWorkerStatusResponse response;
        try {
            String ip = request.getIp();
            int port = request.getPort();
            int status = request.getStatus();
            int workerId = workerService.getWorkerId(ip, port);
            if (workerId == 0) {
                String errMsg = "worker不存在。ip:" + ip + ";port:" + port;
                LOGGER.error(errMsg);
                throw new NotFoundException(errMsg);
            }
            workerService.updateWorkerStatus(workerId, status);
            LOGGER.info("update worker[{}:{}] status to {}", ip, port, status);

            HeartBeatService heartBeatService = Injectors.getInjector().getInstance(HeartBeatService.class);
            WorkerInfo workerInfo = new WorkerInfo(ip, port);
            if (status == WorkerStatus.OFFLINE.getValue()) {
                int groupId = Injectors.getInjector().getInstance(WorkerRegistry.class).getWorkerGroupId(workerInfo);
                if (groupId < 0) {
                    throw new IllegalArgumentException("groupId is not valid: " + groupId);
                } else {
                    heartBeatService.offlineWorker(workerInfo);
                }
            } else if (status == WorkerStatus.ONLINE.getValue()) {
                heartBeatService.onlineWorker(workerInfo);
            }
            response = ServerModifyWorkerStatusResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerModifyWorkerStatusResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
        }
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(RestServerModifyWorkerStatusRequest.class, ServerModifyWorkerStatusResponse.class, MessageType.SYSTEM));
        return list;
    }

}
