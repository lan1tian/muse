/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月4日 上午11:29:29
 */

package com.mogujie.jarvis.server.timer;

import java.util.TimerTask;

import org.joda.time.DateTime;

/**
 * @author guangming
 *
 */
public abstract class AbstractTimerTask extends TimerTask {

    public abstract DateTime getFirstTime(DateTime currentDateTime);

    public abstract long getPeriod();
}
