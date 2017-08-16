/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月10日 上午11:21:23
 */

package com.mogujie.jarvis.logstorage.actor;

import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mogujie.jarvis.protocol.LogProtos.LogStorageHeartBeatResponse;
import com.mogujie.jarvis.protocol.LogProtos.WorkerHeartBeatRequest;


public class HeartbeatActor extends UntypedActor {

    private static final Logger LOGGER = LogManager.getLogger("heartbeat");

    public static Props props() {
        return Props.create(HeartbeatActor.class);
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof WorkerHeartBeatRequest) {
            heardBeat((WorkerHeartBeatRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    private void heardBeat(WorkerHeartBeatRequest request) {
        LogStorageHeartBeatResponse response;
        try {
            Address address = getSender().path().address();
            String ip = address.host().get();
            int port = Integer.parseInt(address.port().get().toString());
            int workerGroupId = request.getWorkerGroupId();
            LOGGER.info("receive heartbeat from worker[ip={},port={},workerGroupID={}]", ip, port, workerGroupId);
            response = LogStorageHeartBeatResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = LogStorageHeartBeatResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            if (!(ex instanceof IllegalArgumentException)) {
                LOGGER.error("heartbeat error", ex);
            }
        }
    }

}
