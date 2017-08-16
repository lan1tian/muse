package com.mogujie.jarvis.worker.task;

import com.mogujie.jarvis.core.AbstractLogCollector;

public class ConsoleLogCollector extends AbstractLogCollector {

    @Override
    public void collectStdout(String line, boolean isEnd) {
        System.out.println("ConsoleLogCollector:\t" + line);
    }

    @Override
    public void collectStderr(String line, boolean isEnd) {
        System.err.println("ConsoleLogCollector:\t" + line);
    }

}
