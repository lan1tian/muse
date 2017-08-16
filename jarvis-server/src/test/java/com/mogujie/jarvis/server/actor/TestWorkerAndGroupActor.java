package com.mogujie.jarvis.server.actor;

import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mogujie.jarvis.core.util.AppTokenUtils;
import com.mogujie.jarvis.dto.generate.Worker;
import com.mogujie.jarvis.protocol.AppAuthProtos;
import com.mogujie.jarvis.protocol.WorkerGroupProtos;
import com.mogujie.jarvis.protocol.WorkerGroupProtos.RestModifyWorkerGroupRequest;
import com.mogujie.jarvis.protocol.WorkerProtos;
import com.mogujie.jarvis.server.actor.base.TestWorkerServiceBase;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/1/12.
 * used by jarvis-parent
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ActorPath.class)
@SuppressStaticInitializationFor("com.mogujie.jarvis.server.guice.Injectors")
public class TestWorkerAndGroupActor extends TestWorkerServiceBase {
    static ActorSystem system = ActorSystem.create("myActor");
    String authKey = AppTokenUtils.generateToken(new Date().getTime(), "workerRegistry");
    AppAuthProtos.AppAuth appAuth = AppAuthProtos.AppAuth.newBuilder().setName("a").setToken(authKey).build();

    @AfterClass
    public static void tearDown() {
        JavaTestKit.shutdownActorSystem(system);
    }

    @Before
    public void ready() {
    }

    @Test
    public void testCreateWorkerGroup() {
        system = ActorSystem.create("myActor");
        WorkerGroupProtos.RestCreateWorkerGroupRequest createWorkerGroupRequest = WorkerGroupProtos.RestCreateWorkerGroupRequest.newBuilder()
                .setWorkerGroupName("group1").setUser("qh").setAppAuth(appAuth).build();
        Props props = WorkerGroupActor.props();
        try {
            when(workerGroupMapper, "insertSelective", any()).thenReturn(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new JavaTestKit(system) {
            {
                ActorRef createActor = system.actorOf(props, "create");
                createActor.tell(createWorkerGroupRequest, getRef());
                WorkerGroupProtos.ServerCreateWorkerGroupResponse response = (WorkerGroupProtos.ServerCreateWorkerGroupResponse) receiveOne(
                        duration("3 seconds"));
                Assert.assertEquals(response.getSuccess(), true);
            }
        };

    }

    @Test
    public void testWorkerModifyStatusActor() {
        system = ActorSystem.create("myActor");
        WorkerProtos.RestServerModifyWorkerStatusRequest modifyWorkerStatusRequest = WorkerProtos.RestServerModifyWorkerStatusRequest.newBuilder()
                .setStatus(1).setIp("127.0.0.1").setPort(10002).setAppAuth(appAuth).build();
        List<Worker> workers = new ArrayList<>();
        Worker worker = new Worker();
        worker.setId(1);
        workers.add(worker);
        try {
            when(workerMapper, "selectByExample", any()).thenReturn(workers);
            when(workerMapper, "updateByPrimaryKeySelective", any()).thenReturn(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new JavaTestKit(system) {
            {

                Props props = WorkerModifyStatusActor.props();
                ActorRef actorRef = system.actorOf(props, "modify");
                actorRef.tell(modifyWorkerStatusRequest, getRef());
                WorkerProtos.ServerModifyWorkerStatusResponse response = (WorkerProtos.ServerModifyWorkerStatusResponse) receiveOne(
                        duration("3 seconds"));
                Assert.assertEquals(response.getSuccess(), true);
            }
        };
        system.terminate();
    }

    @Test
    public void testUpdateWorkerGroup() {
        system = ActorSystem.create("myActor");
        try {
            when(workerGroupMapper, "updateByPrimaryKeySelective", any()).thenReturn(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RestModifyWorkerGroupRequest request = RestModifyWorkerGroupRequest.newBuilder().setAppAuth(appAuth).setUser("qh")
                .setWorkerGroupName("group1").setStatus(1).setWorkerGroupId(1).build();
        Props props = WorkerGroupActor.props();
        new JavaTestKit(system) {
            {
                ActorRef updateActor = system.actorOf(props, "update");
                updateActor.tell(request, getRef());
                WorkerGroupProtos.ServerModifyWorkerGroupResponse response = (WorkerGroupProtos.ServerModifyWorkerGroupResponse) receiveOne(
                        duration("3 seconds"));
                Assert.assertEquals(response.getSuccess(), true);
            }
        };
    }

}
