package com.mogujie.jarvis.server.actor;

import org.junit.After;
import org.junit.Test;

import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.protocol.HeartBeatProtos.HeartBeatRequest;
import com.mogujie.jarvis.protocol.HeartBeatProtos.HeartBeatResponse;
import com.typesafe.config.Config;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/1/8.
 * used by jarvis-parent
 */

public class TestHeartBeatActor {
    protected ActorSystem system;

    HeartBeatRequest heartBeatRequest = HeartBeatRequest.newBuilder().setJobNum(1).build();
    String serverHost = "10.11.6.129";
    String actorPath = "akka.tcp://server@" + serverHost + ":10000/user/server";

    @Test
    public void testHeartBeat() {
        //todo 对于测试是local的模拟，但是实际通信heartbeat需要远程
        Config akkaConfig = ConfigUtils.getAkkaConfig("akka-test.conf");
        system = ActorSystem.create(JarvisConstants.WORKER_AKKA_SYSTEM_NAME, akkaConfig);
        ActorSelection serverActor = system.actorSelection(actorPath);
        new JavaTestKit(system) {
            {
                serverActor.tell(heartBeatRequest, getRef());
                expectMsgEquals(HeartBeatResponse.newBuilder().setSuccess(true).build());
            }
        };

    }

    @After
    public void tearDown() {
        JavaTestKit.shutdownActorSystem(system);
        if (system != null)
            system.terminate();
    }

}
