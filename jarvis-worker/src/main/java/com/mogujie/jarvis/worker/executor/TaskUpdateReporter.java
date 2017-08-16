/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年10月9日 上午9:58:47
 */

package com.mogujie.jarvis.worker.executor;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;

import com.mogujie.jarvis.core.TaskReporter;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.protocol.ReportTaskProtos.WorkerReportTaskUpdateRequest;

public class TaskUpdateReporter implements TaskReporter {

    private ActorSelection actor;
    private ActorRef sender;

    public TaskUpdateReporter(ActorSelection actor, ActorRef sender) {
        this.actor = actor;
        this.sender = sender;
    }

    @Override
    public void report(TaskDetail taskDetail) {
        WorkerReportTaskUpdateRequest request = WorkerReportTaskUpdateRequest.newBuilder()
                .setFullId(taskDetail.getFullId())
                .setContent(taskDetail.getContent())
                .setUser(taskDetail.getUser())
                .build();

        actor.tell(request, sender);
    }

}
