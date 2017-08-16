/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月10日 上午11:21:23
 */

package com.mogujie.jarvis.server.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.protocol.HeartBeatProtos.HeartBeatRequest;
import com.mogujie.jarvis.protocol.HeartBeatProtos.HeartBeatResponse;
import com.mogujie.jarvis.protocol.HeartBeatProtos.RestQueryWorkerHeartbeatInfoRequest;
import com.mogujie.jarvis.protocol.HeartBeatProtos.ServerQueryWorkerHeartbeatInfoResponse;
import com.mogujie.jarvis.protocol.HeartBeatProtos.WorkerHeartbeatEntry;
import com.mogujie.jarvis.server.WorkerRegistry;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.HeartBeatService;

public class HeartBeatActor extends UntypedActor {

    private HeartBeatService heartBeatService = Injectors.getInjector().getInstance(HeartBeatService.class);

    private static final Logger LOGGER = LogManager.getLogger("heartbeat");

    public static Props props() {
        return Props.create(HeartBeatActor.class);
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(HeartBeatRequest.class, HeartBeatResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestQueryWorkerHeartbeatInfoRequest.class, ServerQueryWorkerHeartbeatInfoResponse.class, MessageType.SYSTEM));
        return list;
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof HeartBeatRequest) {
            heardBeat((HeartBeatRequest) obj);
        } else if (obj instanceof RestQueryWorkerHeartbeatInfoRequest) {
            queryWorkerHeartbeatInfo((RestQueryWorkerHeartbeatInfoRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    private void heardBeat(HeartBeatRequest request) {
        HeartBeatResponse response;
        try {
            Address address = getSender().path().address();
            String ip = address.host().get();
            int port = Integer.parseInt(address.port().get().toString());
            int taskNum = request.getJobNum();
            LOGGER.info("receive heartbeat from worker[ip={},port={},taskNum={}]", ip, port, taskNum);
            WorkerInfo workerInfo = new WorkerInfo(ip, port);
            int groupId = Injectors.getInjector().getInstance(WorkerRegistry.class).getWorkerGroupId(workerInfo);
            if (groupId < 0) {
                LOGGER.warn("worker not register: heartbeat[ip={}, port={}, groupId={}, taskNum={}]", ip, port, groupId, taskNum);
                throw new IllegalArgumentException("groupId is not valid: " + groupId);
            }
            LOGGER.debug("heartbeat[ip={}, port={}, groupId={}, taskNum={}]", ip, port, groupId, taskNum);
            heartBeatService.put(groupId, workerInfo, taskNum);
            response = HeartBeatResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = HeartBeatResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            if (!(ex instanceof IllegalArgumentException)) {
                LOGGER.error("", ex);
            }
        }
    }

    private void queryWorkerHeartbeatInfo(RestQueryWorkerHeartbeatInfoRequest request) {
        ServerQueryWorkerHeartbeatInfoResponse response;
        try {
            int workerGroupId = request.getWorkerGroupId();
            if (workerGroupId < 0) {
                throw new IllegalArgumentException("groupId is not valid. groupId: " + workerGroupId);
            }

            ServerQueryWorkerHeartbeatInfoResponse.Builder builder = ServerQueryWorkerHeartbeatInfoResponse.newBuilder();

            Map<WorkerInfo, Integer> map;
            map = heartBeatService.getLeavedWorkerInfo(workerGroupId);

            for (Entry<WorkerInfo, Integer> entry : map.entrySet()) {
                WorkerInfo key = entry.getKey();
                WorkerHeartbeatEntry hb = WorkerHeartbeatEntry.newBuilder().setIp(key.getIp()).setPort(key.getPort()).setTaskNum(entry.getValue())
                        .build();
                builder.addInfo(hb);
            }
            response = builder.setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerQueryWorkerHeartbeatInfoResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            if (!(ex instanceof IllegalArgumentException)) {
                LOGGER.error("", ex);
            }
        }
    }

}
