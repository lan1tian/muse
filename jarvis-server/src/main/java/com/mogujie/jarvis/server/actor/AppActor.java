/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年10月9日 下午5:14:53
 */

package com.mogujie.jarvis.server.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.mybatis.guice.transactional.Transactional;

import akka.actor.Props;
import akka.actor.UntypedActor;

import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.domain.OperationMode;
import com.mogujie.jarvis.core.exception.NotFoundException;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.dto.generate.App;
import com.mogujie.jarvis.dto.generate.AppWorkerGroup;
import com.mogujie.jarvis.protocol.ApplicationProtos;
import com.mogujie.jarvis.protocol.ApplicationProtos.AppCounterEntry;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestCreateApplicationRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestDeleteApplicationRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestModifyApplicationRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestSearchAppCounterRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestSetApplicationWorkerGroupRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerCreateApplicationResponse;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerDeleteApplicationResponse;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerModifyApplicationResponse;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerSearchAppCounterResponse;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerSetApplicationWorkerGroupResponse;
import com.mogujie.jarvis.server.dispatcher.TaskManager;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.AppService;
import com.mogujie.jarvis.server.service.AppWorkerGroupService;
import com.mogujie.jarvis.server.service.ValidService;
import com.mogujie.jarvis.server.service.ValidService.CheckMode;

public class AppActor extends UntypedActor {

    private static final Logger logger = LogManager.getLogger();
    private TaskManager taskManager = Injectors.getInjector().getInstance(TaskManager.class);
    private AppService appService = Injectors.getInjector().getInstance(AppService.class);
    private AppWorkerGroupService appWorkerGroupService = Injectors.getInjector().getInstance(AppWorkerGroupService.class);
    private ValidService validService = Injectors.getInjector().getInstance(ValidService.class);

    public static Props props() {
        return Props.create(AppActor.class);
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(RestCreateApplicationRequest.class, ServerCreateApplicationResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestModifyApplicationRequest.class, ServerModifyApplicationResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestSetApplicationWorkerGroupRequest.class, ServerSetApplicationWorkerGroupResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestSearchAppCounterRequest.class, ServerSearchAppCounterResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestDeleteApplicationRequest.class, ServerDeleteApplicationResponse.class, MessageType.SYSTEM));
        return list;
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof RestCreateApplicationRequest) {
            createApplication((RestCreateApplicationRequest) obj);
        } else if (obj instanceof RestModifyApplicationRequest) {
            modifyApplication((RestModifyApplicationRequest) obj);
        } else if (obj instanceof RestDeleteApplicationRequest) {
            deleteApplication((RestDeleteApplicationRequest) obj);
        } else if (obj instanceof RestSetApplicationWorkerGroupRequest) {
            setApplicationWorkerGroup((RestSetApplicationWorkerGroupRequest) obj);
        } else if (obj instanceof RestSearchAppCounterRequest) {
            searchAppCounter((RestSearchAppCounterRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    @Transactional
    private void createApplication(RestCreateApplicationRequest request) {
        ServerCreateApplicationResponse response = null;
        try {
            App app = msg2App(request);
            validService.checkApp(CheckMode.ADD, app);
            appService.insert(app);
            taskManager.addApp(app.getAppId(), request.getMaxConcurrency());
            response = ServerCreateApplicationResponse.newBuilder().setSuccess(true).setAppId(app.getAppId()).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerCreateApplicationResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            logger.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void modifyApplication(RestModifyApplicationRequest request) {
        ServerModifyApplicationResponse response = null;
        try {
            App app = msg2App(request);
            validService.checkApp(CheckMode.EDIT, app);
            appService.update(app);
            if (request.hasMaxConcurrency()) {
                taskManager.updateAppMaxParallelism(app.getAppId(), request.getMaxConcurrency());
            }
            response = ServerModifyApplicationResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerModifyApplicationResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            logger.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void deleteApplication(RestDeleteApplicationRequest request) {
        ServerDeleteApplicationResponse response = null;
        try {
            int appId = request.getAppId();
            appService.delete(appId);
            response = ServerDeleteApplicationResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerDeleteApplicationResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            logger.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void setApplicationWorkerGroup(RestSetApplicationWorkerGroupRequest request) throws NotFoundException {
        ServerSetApplicationWorkerGroupResponse response = null;
        try {

            List<AppWorkerGroup> list = msg2AppWorkerGroup(request);
            validService.checkAppWorkerGroup(request.getMode(), list);

            OperationMode mode = OperationMode.parseValue(request.getMode());
            for (AppWorkerGroup entry : list) {
                if (mode == OperationMode.ADD) {
                    appWorkerGroupService.insert(entry);
                } else if (mode == OperationMode.DELETE) {
                    appWorkerGroupService.delete(entry.getAppId(), entry.getWorkerGroupId());
                }
            }
            response = ServerSetApplicationWorkerGroupResponse.newBuilder().setSuccess(true).build();
        } catch (Exception ex) {
            response = ServerSetApplicationWorkerGroupResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            logger.error("", ex);
            throw ex;
        } finally {
            getSender().tell(response, getSelf());
        }
    }

    private void searchAppCounter(RestSearchAppCounterRequest request) {
        ServerSearchAppCounterResponse response = null;
        try {
            ServerSearchAppCounterResponse.Builder builder = ServerSearchAppCounterResponse.newBuilder();
            Map<Integer, Long> appCounterMap = taskManager.getAppCounter();
            for (Entry<Integer, Long> entry : appCounterMap.entrySet()) {
                AppCounterEntry appEntry = AppCounterEntry.newBuilder().setAppId(entry.getKey()).setCounter(entry.getValue().intValue()).build();
                builder.addAppCounterEntry(appEntry);
            }
            response = builder.setSuccess(true).build();
        } catch (Exception ex) {
            response = ServerSearchAppCounterResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            logger.error("", ex);
            throw ex;
        } finally {
            getSender().tell(response, getSelf());
        }
    }

    private App msg2App(RestCreateApplicationRequest msg) {
        String key = UUID.randomUUID().toString().replace("-", "");
        DateTime date = DateTime.now();
        App app = new App();
        app.setAppName(msg.getAppName());
        app.setAppKey(key);
        app.setOwner(msg.getOwner());
        app.setStatus(msg.getStatus());
        app.setMaxConcurrency(msg.getMaxConcurrency());
        app.setCreateTime(date.toDate());
        app.setUpdateTime(date.toDate());
        app.setUpdateUser(msg.getUser());
        return app;
    }

    private App msg2App(RestModifyApplicationRequest msg) {
        App app = new App();
        app.setAppId(msg.getAppId());
        if (msg.hasAppName()) {
            app.setAppName(msg.getAppName());
        }
        if (msg.hasOwner()) {
            app.setOwner(msg.getOwner());
        }
        if (msg.hasStatus()) {
            app.setStatus(msg.getStatus());
        }
        if (msg.hasMaxConcurrency()) {
            app.setMaxConcurrency(msg.getMaxConcurrency());
        }
        app.setUpdateTime(DateTime.now().toDate());
        app.setUpdateUser(msg.getUser());
        return app;
    }

    private List<AppWorkerGroup> msg2AppWorkerGroup(RestSetApplicationWorkerGroupRequest msg) {
        List<AppWorkerGroup> list = new ArrayList<>();
        DateTime now = DateTime.now();
        for (ApplicationProtos.AppWorkerGroupEntry entry : msg.getAppWorkerGroupsList()) {
            AppWorkerGroup aw = new AppWorkerGroup();
            aw.setAppId(entry.getAppId());
            aw.setWorkerGroupId(entry.getWorkerGroupId());
            aw.setCreateTime(now.toDate());
            aw.setUpdateTime(now.toDate());
            aw.setUpdateUser(msg.getUser());
            list.add(aw);
        }
        return list;
    }

}
