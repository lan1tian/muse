/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年3月28日 下午5:58:17
 */

package com.mogujie.jarvis.worker;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.mogujie.jarvis.core.ProgressReporter;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.protocol.ReportTaskProtos.ServerReportTaskStatusResponse;
import com.mogujie.jarvis.protocol.ReportTaskProtos.WorkerReportTaskStatusRequest;
import com.mogujie.jarvis.worker.util.FutureUtils;

import akka.actor.ActorSelection;

public final class TaskStatusRetryReporter extends Thread {

    private Map<String, RetryEntry> map = Maps.newConcurrentMap();
    private static final TaskStatusRetryReporter INSTANCE = new TaskStatusRetryReporter();
    private static final Logger LOGGER = LogManager.getLogger();

    private TaskStatusRetryReporter() {
    }

    public static TaskStatusRetryReporter getInstance() {
        return INSTANCE;
    }

    public void addEntry(ActorSelection serverActor, WorkerReportTaskStatusRequest request, ProgressReporter reporter) {
        map.put(request.getFullId(), new RetryEntry(serverActor, request, reporter));
    }

    @Override
    public void run() {
        Iterator<Entry<String, RetryEntry>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, RetryEntry> entry = it.next();
            String fullId = entry.getKey();
            RetryEntry retryEntry = entry.getValue();
            int status = retryEntry.getRequest().getStatus();
            try {
                LOGGER.info("try to report status[fullId={},status={}] to server", fullId, TaskStatus.parseValue(status));
                ServerReportTaskStatusResponse response = (ServerReportTaskStatusResponse) FutureUtils.awaitResult(retryEntry.getServerActor(),
                        retryEntry.getRequest(), 60);
                if (response.getSuccess()) {
                    LOGGER.info("report status[fullId={},status={}] to server successfully", fullId, TaskStatus.parseValue(status));
                    retryEntry.getReporter().report(1);
                }
                it.remove();
            } catch (Exception e) {
                LOGGER.error("", e.getMessage());
            }
        }
    }

    private static class RetryEntry {
        private final ActorSelection serverActor;
        private final WorkerReportTaskStatusRequest request;
        private final ProgressReporter reporter;

        public RetryEntry(ActorSelection serverActor, WorkerReportTaskStatusRequest request, ProgressReporter reporter) {
            this.serverActor = serverActor;
            this.request = request;
            this.reporter = reporter;
        }

        public ActorSelection getServerActor() {
            return serverActor;
        }

        public WorkerReportTaskStatusRequest getRequest() {
            return request;
        }

        public ProgressReporter getReporter() {
            return reporter;
        }

    }
}
