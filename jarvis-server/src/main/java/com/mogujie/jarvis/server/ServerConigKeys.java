/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月15日 下午3:31:31
 */

package com.mogujie.jarvis.server;

public interface ServerConigKeys {

    public static final String SERVER_ACTOR_NUM = "server.actor.num";
    public static final String SERVER_DISPATCHER_THREADS = "server.dispatcher.threads";
    public static final String TASK_METRICS_ACTOR_NUM = "task.metrics.actor.num";
    public static final String TASK_ACTOR_NUM = "task.actor.num";
    public static final String APP_TOKEN_VERIFY_ENABLE = "app.token.verify.enable";
    public static final String APP_MAX_PARALLELISM_LIMIT_ENABLE = "app.max.parallelism.limit.enable";
    public static final String ALARM_APP_NAME = "alarm.app.name";
    public static final String ALARM_ENABLE = "alarm.enable";
    public static final String ALARM_SERVICE_URL = "alarm.service.url";
    public static final String WORKER_SELECTOR = "worker.selector";
    public static final String TASK_REJECT_INTERVAL = "task.reject.interval";
    public static final String TASK_AUTO_INTERVAL = "task.auto.interval";
    public static final String ALARMER_CLASS = "alarmer.class";
    public static final String SERVER_TIMER_TASKS = "sever.timer.tasks";
    public static final String JOB_ACTOR_POST_HOOKS = "job.actor.post.hooks";
    public static final String BIZ_KPI_ID = "biz.kpi.id";

}
