package com.mogujie.jarvis.worker.task;

import com.mogujie.jarvis.core.ProgressReporter;

public class ConsoleProgressReporter implements ProgressReporter {

    @Override
    public void report(float progress) {
        System.out.println("ProgressReporter:\t" + progress);
    }

}
