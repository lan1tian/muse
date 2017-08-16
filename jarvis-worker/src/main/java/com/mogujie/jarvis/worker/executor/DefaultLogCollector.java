/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月1日 下午2:26:16
 */

package com.mogujie.jarvis.worker.executor;

import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.ByteString;
import com.mogujie.jarvis.core.AbstractLogCollector;
import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.domain.StreamType;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.protocol.LogProtos.LogStorageWriteLogResponse;
import com.mogujie.jarvis.protocol.LogProtos.WorkerWriteLogRequest;
import com.mogujie.jarvis.worker.WorkerConfigKeys;
import com.mogujie.jarvis.worker.util.FutureUtils;

import akka.actor.ActorSelection;

/**
 * @author wuya
 */
public class DefaultLogCollector extends AbstractLogCollector {

    private static Logger LOGGER = LogManager.getLogger();

    private ActorSelection actor;
    private String fullId;
    private int maxBytes = ConfigUtils.getWorkerConfig().getInt(WorkerConfigKeys.LOG_SEND_MAX_BYTES, 1024 * 1024);

    public DefaultLogCollector(ActorSelection actor, String fullId) {
        this.actor = actor;
        this.fullId = fullId;
    }

    private void sendLog(String line, boolean isEnd, StreamType streamType) {
        LOGGER.debug("sendLog:fullId={} ,type={} ,isEnd={}, log={}", fullId, streamType.getDescription(), isEnd, line);

        String text = (line != null && line.length() > 0) ? line + JarvisConstants.LINE_SEPARATOR : "";
        byte[] bytes = (text).getBytes(StandardCharsets.UTF_8);
        int srcLen = bytes.length;
        int i = 0;
        boolean sendEnd = false;
        while ((srcLen - maxBytes * i) >= 0) {
            int needSize = maxBytes;
            if ((srcLen - maxBytes * (i + 1)) <= 0) {
                needSize = srcLen - maxBytes * i;
                if (isEnd) {
                    sendEnd = true;
                }
            }

            byte[] dest = new byte[needSize];
            System.arraycopy(bytes, maxBytes * i, dest, 0, needSize);

            WorkerWriteLogRequest request = WorkerWriteLogRequest.newBuilder().setFullId(fullId).setType(streamType.getValue())
                    .setLog(ByteString.copyFrom(dest)).setIsEnd(sendEnd).build();

            LogStorageWriteLogResponse response;
            try {
                response = (LogStorageWriteLogResponse) FutureUtils.awaitResult(actor, request, 10);
                if (response.getSuccess()) {
                    i++;
                }
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }

    @Override
    public void collectStdout(String line, boolean isEnd) {
        sendLog(line, isEnd, StreamType.STD_OUT);
    }

    @Override
    public void collectStderr(String line, boolean isEnd) {
        sendLog(line, isEnd, StreamType.STD_ERR);
    }

}
