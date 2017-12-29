package com.mogujie.jarvis.server;

import com.mogujie.jarvis.server.dispatcher.PriorityTaskQueue;
import com.mogujie.jarvis.server.guice.Injectors;

/**
 * Created by lixun on 2017/9/21.
 */
public class Test {
    public static void main(String[] args) {
        PriorityTaskQueue queue = Injectors.getInjector().getInstance(PriorityTaskQueue.class);
        PriorityTaskQueue queue1 = Injectors.getInjector().getInstance(PriorityTaskQueue.class);
        System.out.println("queue="+queue);
        System.out.println("queue="+queue1);
    }
}
