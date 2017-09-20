/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming Create Date: 2015年10月12日 上午10:18:24
 */

package com.mogujie.jarvis.server.actor;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.mybatis.guice.transactional.Transactional;

import akka.actor.Props;
import akka.actor.UntypedActor;

import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.exception.NotFoundException;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.dto.generate.BizGroup;
import com.mogujie.jarvis.protocol.BizGroupProtos.RestCreateBizGroupRequest;
import com.mogujie.jarvis.protocol.BizGroupProtos.RestDeleteBizGroupRequest;
import com.mogujie.jarvis.protocol.BizGroupProtos.RestModifyBizGroupRequest;
import com.mogujie.jarvis.protocol.BizGroupProtos.ServerCreateBizGroupResponse;
import com.mogujie.jarvis.protocol.BizGroupProtos.ServerDeleteBizGroupResponse;
import com.mogujie.jarvis.protocol.BizGroupProtos.ServerModifyBizGroupResponse;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.BizGroupService;
import com.mogujie.jarvis.server.service.ValidService;
import com.mogujie.jarvis.server.service.ValidService.CheckMode;

public class BizGroupActor extends UntypedActor {
    private static final Logger logger = LogManager.getLogger();

    private BizGroupService BizGroupService = Injectors.getInjector().getInstance(BizGroupService.class);
    private ValidService validService = Injectors.getInjector().getInstance(ValidService.class);

    public static Props props() {
        return Props.create(BizGroupActor.class);
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(RestCreateBizGroupRequest.class, ServerCreateBizGroupResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestModifyBizGroupRequest.class, ServerModifyBizGroupResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestDeleteBizGroupRequest.class, ServerDeleteBizGroupResponse.class, MessageType.SYSTEM));
        return list;
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof RestCreateBizGroupRequest) {
            createBizGroup((RestCreateBizGroupRequest) obj);
        } else if (obj instanceof RestModifyBizGroupRequest) {
            modifyBizGroup((RestModifyBizGroupRequest) obj);
        } else if (obj instanceof RestDeleteBizGroupRequest) {
            deleteBizGroup((RestDeleteBizGroupRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    @Transactional
    public void createBizGroup(RestCreateBizGroupRequest request) throws NotFoundException {
        ServerCreateBizGroupResponse response;
        try {
            BizGroup bizGroup = msg2BizGroup(request);
            validService.checkBizGroup(CheckMode.ADD,bizGroup);
            BizGroupService.insert(bizGroup);

            response = ServerCreateBizGroupResponse.newBuilder().setSuccess(true).setId(bizGroup.getId()).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerCreateBizGroupResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            logger.error("createBizGroup: ", ex);
            throw ex;
        }
    }

    @Transactional
    public void modifyBizGroup(RestModifyBizGroupRequest request) throws NotFoundException {
        ServerModifyBizGroupResponse response;
        try {
            BizGroup bizGroup = msg2BizGroup(request);
            validService.checkBizGroup(CheckMode.EDIT,bizGroup);
            BizGroupService.update(bizGroup);

            response = ServerModifyBizGroupResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerModifyBizGroupResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            logger.error("", ex);
            throw ex;
        }
    }

    @Transactional
    public void deleteBizGroup(RestDeleteBizGroupRequest request) throws NotFoundException {
        ServerDeleteBizGroupResponse response;
        try {
            BizGroup bizGroup = msg2BizGroup(request);
            validService.checkBizGroup(CheckMode.DELETE,bizGroup);
            BizGroupService.delete(bizGroup.getId());
            response = ServerDeleteBizGroupResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerDeleteBizGroupResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            logger.error("", ex);
            throw ex;

        }
    }

    private BizGroup msg2BizGroup(RestCreateBizGroupRequest msg) {
        DateTime now = DateTime.now();
        BizGroup data = new BizGroup();
        data.setName(msg.getName());
        data.setOwner(msg.getOwner());
        data.setStatus(msg.getStatus());
        data.setCreateTime(now.toDate());
        data.setUpdateTime(now.toDate());
        data.setUpdateUser(msg.getUser());
        return data;
    }

    private BizGroup msg2BizGroup(RestModifyBizGroupRequest msg) {
        DateTime now = DateTime.now();
        BizGroup data = new BizGroup();
        data.setId(msg.getId());
        if (msg.hasName()) {
            data.setName(msg.getName());
        }
        if (msg.hasOwner()) {
            data.setOwner(msg.getOwner());
        }
        if (msg.hasStatus()) {
            data.setStatus(msg.getStatus());
        }
        data.setUpdateTime(now.toDate());
        data.setUpdateUser(msg.getUser());
        return data;
    }

    private BizGroup msg2BizGroup(RestDeleteBizGroupRequest msg) {
        BizGroup data = new BizGroup();
        data.setId(msg.getId());
        return data;
    }

}
