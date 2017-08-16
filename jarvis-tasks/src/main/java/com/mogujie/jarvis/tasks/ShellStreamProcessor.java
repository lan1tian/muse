/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月9日 下午1:30:43
 */

package com.mogujie.jarvis.tasks;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import com.mogujie.jarvis.core.domain.StreamType;
import com.mogujie.jarvis.tasks.shell.ShellTask;

/**
 * @author wuya
 *
 */
public class ShellStreamProcessor extends Thread {

    private ShellTask shellJob;
    private InputStream inputStream;
    private StreamType type;
    private CountDownLatch countDownLatch;

    public ShellStreamProcessor(ShellTask job, InputStream inputStream, StreamType type, CountDownLatch countDownLatch) {
        this.shellJob = job;
        this.inputStream = inputStream;
        this.type = type;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        if (type == StreamType.STD_OUT) {
            shellJob.processStdOutputStream(inputStream);
        } else if (type == StreamType.STD_ERR) {
            shellJob.processStdErrorStream(inputStream);
        }

        countDownLatch.countDown();
    }
}
