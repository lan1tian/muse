/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 上午10:35:46
 */

package com.mogujie.jarvis.server.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.mogujie.jarvis.core.observer.Event;
import com.mogujie.jarvis.core.observer.Observable;
import com.mogujie.jarvis.core.observer.Observer;
import com.mogujie.jarvis.core.util.ConfigUtils;

/**
 * job scheduler controller used to schedule job with three schedulers (
 * {@link com.mogujie.jarvis.server.scheduler.time.TimeScheduler},
 * {@link com.mogujie.jarvis.server.scheduler.dag.DAGScheduler}, and
 * {@link com.mogujie.jarvis.server.scheduler.task.TaskScheduler})
 *
 * @author guangming
 *
 */
public class JobSchedulerController implements Observable {
    public static final String SCHEDULER_CONTROLLER_TYPE = "scheduler.controller.type";
    public static final String SCHEDULER_CONTROLLER_TYPE_SYNC = "sync";
    public static final String SCHEDULER_CONTROLLER_TYPE_ASYNC = "async";
    protected static final Logger LOGGER = LogManager.getLogger();
    protected EventBus eventBus;

    private JobSchedulerController() {
        SubscriberExceptionHandler subscriberExceptionHandler = new JarvisSubscriberExceptionHandler();
        Configuration conf = ConfigUtils.getServerConfig();
        String type = conf.getString(SCHEDULER_CONTROLLER_TYPE, SCHEDULER_CONTROLLER_TYPE_ASYNC);
        if (type.equalsIgnoreCase(SCHEDULER_CONTROLLER_TYPE_SYNC)) {
            eventBus = new EventBus(subscriberExceptionHandler);
        } else {
            ExecutorService executorService = Executors.newCachedThreadPool();
            eventBus = new AsyncEventBus(executorService,subscriberExceptionHandler);
        }
    }

    private static final JobSchedulerController single = new JobSchedulerController();

    public static JobSchedulerController getInstance() {
        return single;
    }

    @Override
    public void register(Observer o) {
        if (o instanceof Scheduler) {
            Scheduler scheduler = (Scheduler) o;
            if (scheduler.getSchedulerController() == null) {
                scheduler.setSchedulerController(this);
            }
            eventBus.register(o);
            LOGGER.info("{} register {}", getClass().getSimpleName(), o.getClass().getSimpleName());
        }
    }

    @Override
    public void unregister(Observer o) {
        if (o instanceof Scheduler) {
            eventBus.unregister(o);
            LOGGER.info("{} unregister {}", getClass().getSimpleName(), o.getClass().getSimpleName());
        }
    }

    @Override
    public void notify(Event event) {
        eventBus.post(event);
    }

    static class JarvisSubscriberExceptionHandler implements SubscriberExceptionHandler {

        @Override
        public void handleException(Throwable t, SubscriberExceptionContext context) {
            LOGGER.error("handle error: " + context.getEvent(), t);
        }
    }

}
