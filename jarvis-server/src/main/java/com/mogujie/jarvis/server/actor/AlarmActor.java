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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.mybatis.guice.transactional.Transactional;

import akka.actor.Props;
import akka.actor.UntypedActor;

import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.dto.generate.Alarm;
import com.mogujie.jarvis.protocol.AlarmProtos.RestCreateAlarmRequest;
import com.mogujie.jarvis.protocol.AlarmProtos.RestDeleteAlarmRequest;
import com.mogujie.jarvis.protocol.AlarmProtos.RestModifyAlarmRequest;
import com.mogujie.jarvis.protocol.AlarmProtos.ServerCreateAlarmResponse;
import com.mogujie.jarvis.protocol.AlarmProtos.ServerDeleteAlarmResponse;
import com.mogujie.jarvis.protocol.AlarmProtos.ServerModifyAlarmResponse;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.AlarmService;
import com.mogujie.jarvis.server.service.ValidService;
import com.mogujie.jarvis.server.service.ValidService.CheckMode;


public class AlarmActor extends UntypedActor {

    private ValidService validService = Injectors.getInjector().getInstance(ValidService.class);
    private AlarmService alarmService = Injectors.getInjector().getInstance(AlarmService.class);
    private static final Logger logger = LogManager.getLogger();

    public static Props props() {
        return Props.create(AlarmActor.class);
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(RestCreateAlarmRequest.class, ServerCreateAlarmResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestModifyAlarmRequest.class, ServerModifyAlarmResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(RestDeleteAlarmRequest.class, ServerDeleteAlarmResponse.class, MessageType.SYSTEM));
        return list;
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof RestCreateAlarmRequest) {
            createAlarm((RestCreateAlarmRequest) obj);
        } else if (obj instanceof RestModifyAlarmRequest) {
            modifyAlarm((RestModifyAlarmRequest) obj);
        } else if (obj instanceof RestDeleteAlarmRequest) {
            deleteAlarm((RestDeleteAlarmRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    @Transactional
    private void createAlarm(RestCreateAlarmRequest request) {
        ServerCreateAlarmResponse response;
        try {
            Alarm alarm = msg2Alarm(request);
            validService.checkAlarm(CheckMode.ADD, alarm);
            alarmService.insert(alarm);

            response = ServerCreateAlarmResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerCreateAlarmResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            logger.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void modifyAlarm(RestModifyAlarmRequest request) {
        ServerModifyAlarmResponse response;
        try {
            Alarm alarm = msg2Alarm(request);
            validService.checkAlarm(CheckMode.EDIT, alarm);
            alarmService.updateByJobId(alarm);

            response = ServerModifyAlarmResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerModifyAlarmResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            logger.error("", ex);
            throw ex;
        }
    }

    @Transactional
    private void deleteAlarm(RestDeleteAlarmRequest request) {
        ServerDeleteAlarmResponse response;
        try {
            Alarm alarm = msg2Alarm(request);
            validService.checkAlarm(CheckMode.DELETE, alarm);
            alarmService.deleteByJobId(alarm.getJobId());

            response = ServerDeleteAlarmResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerDeleteAlarmResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            logger.error("", ex);
            throw ex;
        }
    }


    private Alarm msg2Alarm(RestCreateAlarmRequest msg) {
        DateTime now = DateTime.now();
        Alarm alarm = new Alarm();
        alarm.setJobId(msg.getJobId());
        alarm.setAlarmType(msg.getAlarmType());
        alarm.setReceiver(msg.getReciever());
        alarm.setStatus(msg.getStatus());

        alarm.setCreateTime(now.toDate());
        alarm.setUpdateTime(now.toDate());
        alarm.setUpdateUser(msg.getUser());
        return alarm;
    }

    private Alarm msg2Alarm(RestModifyAlarmRequest msg) {
        DateTime now = DateTime.now();
        Alarm alarm = new Alarm();
        alarm.setJobId(msg.getJobId());
        if (msg.hasAlarmType()) {
            alarm.setAlarmType(msg.getAlarmType());
        }
        if (msg.hasReciever()) {
            alarm.setReceiver(msg.getReciever());
        }
        if (msg.hasStatus()) {
            alarm.setStatus(msg.getStatus());
        }
        alarm.setUpdateTime(now.toDate());
        alarm.setUpdateUser(msg.getUser());
        return alarm;
    }

    private Alarm msg2Alarm(RestDeleteAlarmRequest msg) {
        Alarm alarm = new Alarm();
        alarm.setJobId(msg.getJobId());
        return alarm;
    }


}
