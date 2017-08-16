/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月7日 下午2:31:05
 */

package com.mogujie.jarvis.worker.executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;

import com.mogujie.jarvis.core.ProgressReporter;
import com.mogujie.jarvis.protocol.ReportTaskProtos.WorkerReportTaskProgressRequest;

public class DefaultProgressReporter implements ProgressReporter {

    private ActorSelection actor;
    private ActorRef sender;
    private String fullId;
    private static final Logger LOGGER = LogManager.getLogger();

    public DefaultProgressReporter(ActorSelection actor, ActorRef sender, String fullId) {
        this.actor = actor;
        this.sender = sender;
        this.fullId = fullId;
    }

    @Override
    public void report(float progress) {
        WorkerReportTaskProgressRequest request = WorkerReportTaskProgressRequest.newBuilder().setFullId(fullId).setProgress(progress).build();
        actor.tell(request, sender);
        LOGGER.info("report progress[fullId={},progress={}] to server.", fullId, progress);
    }

}
