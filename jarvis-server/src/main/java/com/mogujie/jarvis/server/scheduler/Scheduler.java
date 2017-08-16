/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月8日 上午10:26:00
 */

package com.mogujie.jarvis.server.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.mogujie.jarvis.core.observer.Observer;
import com.mogujie.jarvis.server.scheduler.event.StartEvent;
import com.mogujie.jarvis.server.scheduler.event.StopEvent;

/**
 * @author guangming
 *
 */
public abstract class Scheduler implements Observer {
    private JobSchedulerController schedulerController;
    private static final Logger LOGGER = LogManager.getLogger();

    public void setSchedulerController(JobSchedulerController schedulerController) {
        this.schedulerController = schedulerController;
    }

    public JobSchedulerController getSchedulerController() {
        return schedulerController;
    }

    @Subscribe
    public abstract void handleStartEvent(StartEvent event);

    @Subscribe
    public abstract void handleStopEvent(StopEvent event);

    @Subscribe
    public void handleDeadEvent(DeadEvent event) {
        LOGGER.warn("Receive DeadEvent {} from {}", event.getEvent(), event.getSource().getClass());
    }
}
