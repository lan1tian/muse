/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年3月24日 上午11:33:50
 */

package com.mogujie.jarvis.server.actor;

import com.mogujie.jarvis.protocol.DepartmentProtos;
import com.mogujie.jarvis.server.service.ValidService;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.mybatis.guice.transactional.Transactional;

import akka.actor.Props;
import akka.actor.UntypedActor;

import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.dto.generate.Department;
import com.mogujie.jarvis.dto.generate.DepartmentBizMap;
import com.mogujie.jarvis.protocol.DepartmentProtos.RestCreateDepartmentBizMapRequest;
import com.mogujie.jarvis.protocol.DepartmentProtos.RestCreateDepartmentRequest;
import com.mogujie.jarvis.protocol.DepartmentProtos.RestDeleteDepartmentBizMapRequest;
import com.mogujie.jarvis.protocol.DepartmentProtos.RestDeleteDepartmentRequest;
import com.mogujie.jarvis.protocol.DepartmentProtos.RestModifyDepartmentRequest;
import com.mogujie.jarvis.protocol.DepartmentProtos.ServerCreateDepartmentBizMapResponse;
import com.mogujie.jarvis.protocol.DepartmentProtos.ServerCreateDepartmentResponse;
import com.mogujie.jarvis.protocol.DepartmentProtos.ServerDeleteDepartmentBizMapResponse;
import com.mogujie.jarvis.protocol.DepartmentProtos.ServerDeleteDepartmentResponse;
import com.mogujie.jarvis.protocol.DepartmentProtos.ServerModifyDepartmentResponse;
import com.mogujie.jarvis.protocol.DepartmentProtos.RestDeleteDepartmentBizMapByDepartmentIdRequest;
import com.mogujie.jarvis.protocol.DepartmentProtos.ServerDeleteDepartmentBizMapByDepartmentIdResponse;
import com.mogujie.jarvis.protocol.DepartmentProtos.RestDeleteDepartmentBizMapByBizGroupIdRequest;
import com.mogujie.jarvis.protocol.DepartmentProtos.ServerDeleteDepartmentBizMapByBizGroupIdResponse;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.DepartmentService;

/**
 * @author guangming
 *
 */
public class DepartmentActor extends UntypedActor {

    private static final Logger LOGGER = LogManager.getLogger();

    private DepartmentService departmentService = Injectors.getInjector().getInstance(DepartmentService.class);

    private ValidService validService = Injectors.getInjector().getInstance(ValidService.class);

    public static Props props() {
        return Props.create(DepartmentActor.class);
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(RestCreateDepartmentRequest.class, ServerCreateDepartmentResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestModifyDepartmentRequest.class, ServerModifyDepartmentResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestDeleteDepartmentRequest.class, ServerDeleteDepartmentResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestCreateDepartmentBizMapRequest.class, ServerCreateDepartmentBizMapResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestDeleteDepartmentBizMapRequest.class, ServerDeleteDepartmentBizMapResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestDeleteDepartmentBizMapByDepartmentIdRequest.class, ServerDeleteDepartmentBizMapByDepartmentIdResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestDeleteDepartmentBizMapByBizGroupIdRequest.class, ServerDeleteDepartmentBizMapByBizGroupIdResponse.class, MessageType.SYSTEM));
        return list;
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        LOGGER.info("receive {}", obj.getClass().getSimpleName());
        if (obj instanceof RestCreateDepartmentRequest) {
            createDepartment((RestCreateDepartmentRequest) obj);
        } else if (obj instanceof RestModifyDepartmentRequest) {
            modifyDepartment((RestModifyDepartmentRequest) obj);
        } else if (obj instanceof RestDeleteDepartmentRequest) {
            deleteDepartment((RestDeleteDepartmentRequest) obj);
        } else if (obj instanceof RestCreateDepartmentBizMapRequest) {
            createDeparmentBizMap((RestCreateDepartmentBizMapRequest) obj);
        } else if (obj instanceof RestDeleteDepartmentBizMapRequest) {
            deleteDepartmentBizMap((RestDeleteDepartmentBizMapRequest) obj);
        } else if(obj instanceof DepartmentProtos.RestDeleteDepartmentBizMapByDepartmentIdRequest) {
            deleteMapByDepartId((DepartmentProtos.RestDeleteDepartmentBizMapByDepartmentIdRequest) obj);
        } else if(obj instanceof DepartmentProtos.RestDeleteDepartmentBizMapByBizGroupIdRequest){
            deleteMapByBizGroupId((DepartmentProtos.RestDeleteDepartmentBizMapByBizGroupIdRequest) obj);
        } else {
            unhandled(obj);
        }

    }

    @Transactional
    private void createDepartment(RestCreateDepartmentRequest request) {
        ServerCreateDepartmentResponse response;
        try {
            Department department = new Department();
            department.setName(request.getName());
            department.setUpdateUser(request.getUser());
            DateTime now = DateTime.now();
            department.setCreateTime(now.toDate());
            department.setUpdateTime(now.toDate());

            validService.checkDepartment(ValidService.CheckMode.ADD, department);

            departmentService.insert(department);

            response = ServerCreateDepartmentResponse.newBuilder().setSuccess(true).setId(department.getId()).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerCreateDepartmentResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void modifyDepartment(RestModifyDepartmentRequest request) {
        ServerModifyDepartmentResponse response;
        try {
            Department department = new Department();
            department.setName(request.getName());
            department.setUpdateUser(request.getUser());
            department.setUpdateTime(DateTime.now().toDate());
            departmentService.update(department);

            response = ServerModifyDepartmentResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerModifyDepartmentResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void deleteDepartment(RestDeleteDepartmentRequest request) {
        ServerDeleteDepartmentResponse response;
        try {
            departmentService.deleteDepartmen(request.getId());
            response = ServerDeleteDepartmentResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerDeleteDepartmentResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void createDeparmentBizMap(RestCreateDepartmentBizMapRequest request) {
        ServerCreateDepartmentBizMapResponse response;
        try {
            DepartmentBizMap map = new DepartmentBizMap();
            map.setDepartmentId(request.getDepartmentId());
            map.setBizId(request.getBizId());
            map.setUpdateUser(request.getUser());
            DateTime now = DateTime.now();
            map.setCreateTime(now.toDate());
            map.setUpdateTime(now.toDate());

            this.validService.checkDepartmentBizMap(ValidService.CheckMode.ADD, map);

            departmentService.insertMap(map);

            response = ServerCreateDepartmentBizMapResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerCreateDepartmentBizMapResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void deleteDepartmentBizMap(RestDeleteDepartmentBizMapRequest request) {
        ServerDeleteDepartmentBizMapResponse response;
        try {
            departmentService.deleteMap(request.getDepartmentId(), request.getBizId());
            response = ServerDeleteDepartmentBizMapResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerDeleteDepartmentBizMapResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void deleteMapByDepartId(RestDeleteDepartmentBizMapByDepartmentIdRequest request) {
        ServerDeleteDepartmentBizMapByDepartmentIdResponse response;
        try {
            this.departmentService.deleteMapByDepartmentId(request.getDepartmentId());
            response = ServerDeleteDepartmentBizMapByDepartmentIdResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerDeleteDepartmentBizMapByDepartmentIdResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void deleteMapByBizGroupId(RestDeleteDepartmentBizMapByBizGroupIdRequest request) {
        ServerDeleteDepartmentBizMapByBizGroupIdResponse response;
        try {
            this.departmentService.deleteMapByBizGroupId(request.getBizId());
            response = ServerDeleteDepartmentBizMapByBizGroupIdResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerDeleteDepartmentBizMapByBizGroupIdResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
            throw ex;
        }
    }

}
