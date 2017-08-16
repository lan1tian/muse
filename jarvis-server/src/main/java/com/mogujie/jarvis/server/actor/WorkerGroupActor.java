/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming Create Date: 2015年10月12日 上午10:18:24
 */

package com.mogujie.jarvis.server.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import akka.actor.Props;
import akka.actor.UntypedActor;

import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.dto.generate.WorkerGroup;
import com.mogujie.jarvis.protocol.WorkerGroupProtos.RestCreateWorkerGroupRequest;
import com.mogujie.jarvis.protocol.WorkerGroupProtos.RestModifyWorkerGroupRequest;
import com.mogujie.jarvis.protocol.WorkerGroupProtos.ServerCreateWorkerGroupResponse;
import com.mogujie.jarvis.protocol.WorkerGroupProtos.ServerModifyWorkerGroupResponse;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.WorkerGroupService;

public class WorkerGroupActor extends UntypedActor {

    private WorkerGroupService workerGroupService = Injectors.getInjector().getInstance(WorkerGroupService.class);
    private static final Logger LOGGER = LogManager.getLogger();

    public static Props props() {
        return Props.create(WorkerGroupActor.class);
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(RestCreateWorkerGroupRequest.class, ServerCreateWorkerGroupResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestModifyWorkerGroupRequest.class, ServerModifyWorkerGroupResponse.class, MessageType.SYSTEM));
        return list;
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof RestCreateWorkerGroupRequest) {
            createWorkerGroup((RestCreateWorkerGroupRequest) obj);
        } else if (obj instanceof RestModifyWorkerGroupRequest) {
            modifyWorkerGroup((RestModifyWorkerGroupRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    private void createWorkerGroup(RestCreateWorkerGroupRequest request) {
        ServerCreateWorkerGroupResponse response;
        try {
            WorkerGroup workerGroup = new WorkerGroup();
            String key = UUID.randomUUID().toString().replace("-", "");
            workerGroup.setAuthKey(key);
            workerGroup.setName(request.getWorkerGroupName());
            DateTime now = DateTime.now();
            workerGroup.setCreateTime(now.toDate());
            workerGroup.setUpdateTime(now.toDate());
            workerGroup.setUpdateUser(request.getUser());
            workerGroupService.insert(workerGroup);

            LOGGER.info("create workerGroup, key={}, name={}", key, request.getWorkerGroupName());
            response = ServerCreateWorkerGroupResponse.newBuilder().setSuccess(true).setWorkerGroupKey(key).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerCreateWorkerGroupResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
        }
    }

    private void modifyWorkerGroup(RestModifyWorkerGroupRequest request) {
        ServerModifyWorkerGroupResponse response;
        try {
            WorkerGroup workerGroup = new WorkerGroup();
            workerGroup.setId(request.getWorkerGroupId());
            if (request.hasWorkerGroupName()) {
                workerGroup.setName(request.getWorkerGroupName());
            }
            if (request.hasUser()) {
                workerGroup.setUpdateUser(request.getUser());
            }
            if (request.hasStatus()) {
                workerGroup.setStatus(request.getStatus());
            }
            workerGroup.setUpdateTime(DateTime.now().toDate());
            workerGroup.setUpdateUser(request.getUser());
            workerGroupService.update(workerGroup);
            LOGGER.info("modify workerGroup, id={}", request.getWorkerGroupId());
            response = ServerModifyWorkerGroupResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerModifyWorkerGroupResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
        }
    }

}
