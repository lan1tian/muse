package com.mogujie.jarvis.server.actor;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.protocol.AppAuthProtos;
import com.mogujie.jarvis.protocol.RetryTaskProtos;
import com.mogujie.jarvis.server.actor.util.TestJarvisConstants;
import com.mogujie.jarvis.server.guice4test.Injectors4Test;
import com.mogujie.jarvis.server.service.TaskService;
import com.mogujie.jarvis.server.util.FutureUtils;
import com.typesafe.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/2/1.
 * used by jarvis-parent
 */
public class TestTaskRetryActorWithMain {
    static TaskService taskService = Injectors4Test.getInjector().getInstance(TaskService.class);

    public static void main(String[] args) {
        AppAuthProtos.AppAuth appAuth = AppAuthProtos.AppAuth.newBuilder().setToken("11111").setName("jarvis-web").build();
        String actorPath = TestJarvisConstants.TEST_SERVER_ACTOR_PATH;
        Config akkaConfig = ConfigUtils.getAkkaConfig("akka-test.conf");
        ActorSystem system = ActorSystem.create(TestJarvisConstants.TEST_AKKA_SYSTEM_NAME, akkaConfig);
        List<Integer> statusList = new ArrayList<>();
        statusList.add(TaskStatus.FAILED.getValue());
        //随机取一个taskid
        long randomTaskId = new TestTaskRetryActorWithMain().getRandomTaskId(statusList);
        Thread retryThread = new Thread(new RetryProxy(randomTaskId, system, actorPath, appAuth));
        Thread checkThread = new Thread(new CheckRetryService(randomTaskId, system, actorPath, appAuth));
        checkThread.setPriority(Thread.MAX_PRIORITY);
        retryThread.start();
        checkThread.start();


    }

    private long getRandomTaskId(List<Integer> statusList) {
        List<Task> tasksByStatus = taskService.getTasksByStatus(statusList);
        long randomTaskId = tasksByStatus.get(Math.abs(
                new Random().nextInt(tasksByStatus.size() - 1))).getTaskId();
        return randomTaskId;
    }


    static class CheckRetryService implements Runnable {
        long randomTaskId;
        Task retryTask;
        int flag = 0;
        ActorSystem system;
        String actorPath;
        AppAuthProtos.AppAuth appAuth;

        public CheckRetryService(long randomTaskId, ActorSystem system, String actorPath, AppAuthProtos.AppAuth appAuth) {
            this.randomTaskId = randomTaskId;
            this.system = system;
            this.actorPath = actorPath;
            this.appAuth = appAuth;
        }


        @Override
        public void run() {
            while (true) {

                retryTask = taskService.get(randomTaskId);
                System.out.println(retryTask.getStatus() + "-" + retryTask.getTaskId());
                if (retryTask.getStatus() >= TaskStatus.WAITING.getValue()
                        && retryTask.getStatus() <= TaskStatus.SUCCESS.getValue()) {
                    assertThat((int) retryTask.getStatus(),
                            allOf(greaterThanOrEqualTo(TaskStatus.WAITING.getValue()),
                                    lessThan(TaskStatus.FAILED.getValue())));
                    flag = 100;
                    System.out.println("run success");
                    break;
                }
                if (flag > 60) {
                    System.err.println("catch status error");
                    break;
                }
                flag++;

            }
            System.exit(0);
        }
    }

    static class RetryProxy implements Runnable {
        long randomTaskId;
        ActorSystem system;
        String actorPath;
        AppAuthProtos.AppAuth appAuth;

        public RetryProxy(long randomTaskId, ActorSystem system, String actorPath, AppAuthProtos.AppAuth appAuth) {
            this.randomTaskId = randomTaskId;
            this.system = system;
            this.actorPath = actorPath;
            this.appAuth = appAuth;
        }

        @Override
        public void run() {

            RetryTaskProtos.ServerRetryTaskResponse response = null;
            ActorSelection serverActor = system.actorSelection(actorPath);
            assertEquals(TaskStatus.FAILED.getValue(), (int) taskService.get(randomTaskId).getStatus());
            RetryTaskProtos.RestServerRetryTaskRequest request = RetryTaskProtos.RestServerRetryTaskRequest.newBuilder()
                    .setAppAuth(appAuth)
                    .setTaskId(randomTaskId)
                    .build();

            try {
                response = (RetryTaskProtos.ServerRetryTaskResponse) FutureUtils.awaitResult(serverActor, request, 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.err.println("retry over");
            assertTrue(response.getSuccess());
        }
    }
}
