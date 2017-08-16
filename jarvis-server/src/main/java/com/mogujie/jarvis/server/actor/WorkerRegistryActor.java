/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya Create Date: 2015年9月22日 上午9:55:19
 */

package com.mogujie.jarvis.server.actor;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.protocol.WorkerProtos.ServerRegistryResponse;
import com.mogujie.jarvis.protocol.WorkerProtos.WorkerRegistryRequest;
import com.mogujie.jarvis.server.WorkerRegistry;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.WorkerGroupService;
import com.mogujie.jarvis.server.service.WorkerService;

import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Worker authentication
 */
public class WorkerRegistryActor extends UntypedActor {

    private WorkerService workerService = Injectors.getInjector().getInstance(WorkerService.class);
    private WorkerGroupService workerGroupService = Injectors.getInjector().getInstance(WorkerGroupService.class);

    private static Logger LOGGER = LogManager.getLogger();

    public static Props props() {
        return Props.create(WorkerRegistryActor.class);
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(WorkerRegistryRequest.class, ServerRegistryResponse.class, MessageType.SYSTEM));
        return list;
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof WorkerRegistryRequest) {
            WorkerRegistry((WorkerRegistryRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    private void WorkerRegistry(WorkerRegistryRequest request) {
        ServerRegistryResponse response;
        try {
            String key = request.getKey();
            int groupId = workerGroupService.getGroupIdByAuthKey(key);
            if (groupId == 0) {
                LOGGER.error("worker group key不合法。key={}", key);
                throw new IllegalArgumentException("worker group key不合法。key=" + key);
            }

            Address address = getSender().path().address();
            String ip = address.host().get();
            int port = Integer.parseInt(address.port().get().toString());
            WorkerInfo workerInfo = new WorkerInfo(ip, port);
            WorkerRegistry workerRegistry = Injectors.getInjector().getInstance(WorkerRegistry.class);
            workerRegistry.put(workerInfo, groupId);
            workerService.saveWorker(ip, port, groupId);

            LOGGER.info("register worker[ip={},port={}] successfully.", ip, port);

            response = ServerRegistryResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());

        } catch (Exception ex) {
            response = ServerRegistryResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
            throw ex;
        }
    }

}
