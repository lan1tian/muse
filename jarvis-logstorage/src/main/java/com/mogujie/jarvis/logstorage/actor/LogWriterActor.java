/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年8月31日 下午4:10:00
 */

package com.mogujie.jarvis.logstorage.actor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogujie.jarvis.core.domain.StreamType;
import com.mogujie.jarvis.logstorage.logStream.LocalLogStream;
import com.mogujie.jarvis.logstorage.logStream.LogStream;
import com.mogujie.jarvis.protocol.LogProtos.LogStorageWriteLogResponse;
import com.mogujie.jarvis.protocol.LogProtos.WorkerWriteLogRequest;

import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * @author 牧名
 */
public class LogWriterActor extends UntypedActor {

    private static final Logger LOGGER = LogManager.getLogger();

    public static Props props() {
        return Props.create(LogWriterActor.class);
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof WorkerWriteLogRequest) {
            writeLog((WorkerWriteLogRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    /**
     * 消息处理——写日志
     *
     * @param msg
     * @throws Exception
     */
    private void writeLog(WorkerWriteLogRequest msg) throws Exception {

        //响应值_做成
        LogStorageWriteLogResponse response;

        try {
            String fullId = msg.getFullId();
            StreamType streamType = StreamType.parseValue(msg.getType());
            String log = msg.getLog().toStringUtf8();
            Boolean isEnd = msg.getIsEnd();

            LogStream logStream = new LocalLogStream(fullId, streamType);

            //写log到本地文件
            logStream.writeText(log);

            //log是否结束
            if (isEnd) {
                logStream.writeEndFlag();
            }
            LOGGER.debug("writeLog:fullId={}, type={}, isEnd={}, log={}", fullId, streamType.getDescription(), isEnd, log);

            response = LogStorageWriteLogResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception e) {
            response = LogStorageWriteLogResponse.newBuilder().setSuccess(false).setMessage(e.getMessage() != null ? e.getMessage() : e.toString())
                    .build();
            getSender().tell(response, getSelf());
            LOGGER.error(e);
            throw e;

        }

    }

}
