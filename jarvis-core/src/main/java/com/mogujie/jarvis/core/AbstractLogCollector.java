/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月9日 下午11:17:15
 */

package com.mogujie.jarvis.core;

/**
 * Abstract Log Collector
 */
public abstract class AbstractLogCollector {

    public void collectStdout(String line) {
        collectStdout(line, false);
    }

    public void collectStderr(String line) {
        collectStderr(line, false);
    }

    public abstract void collectStdout(String line, boolean isEnd);

    public abstract void collectStderr(String line, boolean isEnd);
}
