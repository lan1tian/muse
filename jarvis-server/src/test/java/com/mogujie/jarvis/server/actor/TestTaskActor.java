package com.mogujie.jarvis.server.actor;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.mogujie.jarvis.core.domain.JobPriority;
import com.mogujie.jarvis.core.domain.JobRelationType;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.core.domain.OperationMode;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.expression.ScheduleExpressionType;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.dto.generate.JobScheduleExpression;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.protocol.AppAuthProtos.AppAuth;
import com.mogujie.jarvis.protocol.JobProtos;
import com.mogujie.jarvis.protocol.JobProtos.RestModifyJobScheduleExpRequest;
import com.mogujie.jarvis.protocol.JobProtos.RestRemoveJobRequest;
import com.mogujie.jarvis.protocol.JobProtos.RestSubmitJobRequest;
import com.mogujie.jarvis.protocol.JobProtos.ServerSubmitJobResponse;
import com.mogujie.jarvis.protocol.JobScheduleExpressionEntryProtos.ScheduleExpressionEntry;
import com.mogujie.jarvis.protocol.KillTaskProtos;
import com.mogujie.jarvis.protocol.ModifyTaskStatusProtos.RestServerModifyTaskStatusRequest;
import com.mogujie.jarvis.protocol.ModifyTaskStatusProtos.ServerModifyTaskStatusResponse;
import com.mogujie.jarvis.protocol.QueryTaskRelationProtos;
import com.mogujie.jarvis.protocol.QueryTaskRelationProtos.RestServerQueryTaskRelationRequest;
import com.mogujie.jarvis.protocol.RemoveTaskProtos.RestServerRemoveTaskRequest;
import com.mogujie.jarvis.protocol.RemoveTaskProtos.ServerRemoveTaskResponse;
import com.mogujie.jarvis.server.actor.util.TestJarvisConstants;
import com.mogujie.jarvis.server.guice4test.Injectors4Test;
import com.mogujie.jarvis.server.service.JobService;
import com.mogujie.jarvis.server.service.TaskDependService;
import com.mogujie.jarvis.server.service.TaskService;
import com.mogujie.jarvis.server.util.FutureUtils;
import com.typesafe.config.Config;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/1/15.
 * used by jarvis-parent
 */
public class TestTaskActor {
    String actorPath = TestJarvisConstants.TEST_SERVER_ACTOR_PATH;
    ActorSystem system;
    TaskService taskService = Injectors4Test.getInjector().getInstance(TaskService.class);
    AppAuth appAuth = AppAuth.newBuilder().setToken("11111").setName("jarvis-web").build();
    TaskDependService taskDependService = Injectors4Test.getInjector().getInstance(TaskDependService.class);

    @Before
    public void setup() {
        system = getActorSystem();
    }

    @After
    public void close() throws TimeoutException, InterruptedException {
        Await.ready(system.terminate(), Duration.Inf());
    }

    @Test
    public void testQueryTaskRelation() {
        List<Integer> statusList = Lists.newArrayList();
        statusList.add(TaskStatus.SUCCESS.getValue());
        statusList.add(TaskStatus.READY.getValue());
        statusList.add(TaskStatus.RUNNING.getValue());

        //随机取一个id
        long queryId = getRandomTaskId(statusList);

        ActorSelection serverActor = system.actorSelection(actorPath);
        RestServerQueryTaskRelationRequest childRequest = RestServerQueryTaskRelationRequest.newBuilder().setAppAuth(appAuth)
                .setRelationType(JobRelationType.CHILD.getValue()).setTaskId(queryId).build();
        RestServerQueryTaskRelationRequest parentRequest = RestServerQueryTaskRelationRequest.newBuilder().setAppAuth(appAuth)
                .setRelationType(JobRelationType.PARENT.getValue()).setTaskId(queryId).build();
        QueryTaskRelationProtos.ServerQueryTaskRelationResponse response = null;
        QueryTaskRelationProtos.ServerQueryTaskRelationResponse parentResponse = null;

        try {
            response = (QueryTaskRelationProtos.ServerQueryTaskRelationResponse) FutureUtils.awaitResult(serverActor, childRequest, 15);
            parentResponse = (QueryTaskRelationProtos.ServerQueryTaskRelationResponse) FutureUtils.awaitResult(serverActor, parentRequest, 15);

        } catch (Exception e) {
            e.printStackTrace();
        }
        List<QueryTaskRelationProtos.TaskMapEntry> taskRelationMapList = response.getTaskRelationMapList();

        Map<Long, List<Long>> childMap = taskDependService.loadChild(queryId);
        List<Long> expectList = Lists.newArrayList();
        List<Long> actualList = Lists.newArrayList();

        for (Map.Entry<Long, List<Long>> entry : childMap.entrySet()) {
            expectList.add((long) entry.getKey());
        }
        for (QueryTaskRelationProtos.TaskMapEntry entry : taskRelationMapList) {
            actualList.add(entry.getJobId());
        }

        assertArrayEquals(expectList.toArray(), actualList.toArray());

        taskRelationMapList = parentResponse.getTaskRelationMapList();
        expectList.clear();
        actualList.clear();

        Map<Long, List<Long>> parentsMap = taskDependService.loadParent(queryId);

        for (Map.Entry<Long, List<Long>> entry : parentsMap.entrySet()) {
            expectList.add((long) entry.getKey());
        }
        for (QueryTaskRelationProtos.TaskMapEntry entry : taskRelationMapList) {
            actualList.add(entry.getJobId());
        }

        assertArrayEquals(expectList.toArray(), actualList.toArray());

    }

    private long getRandomTaskId(List<Integer> statusList) {
        List<Task> tasksByStatus = taskService.getTasksByStatus(statusList);
        long randomTaskId = tasksByStatus.get(Math.abs(new Random().nextInt(tasksByStatus.size() - 1))).getTaskId();
        return randomTaskId;
    }

    @Test
    public void testModifyTaskStatus() {
        List<Integer> statusList = Lists.newArrayList();

        statusList.add(TaskStatus.FAILED.getValue());
        long taskId = getRandomTaskId(statusList);
        ServerModifyTaskStatusResponse response = null;
        RestServerModifyTaskStatusRequest request = RestServerModifyTaskStatusRequest.newBuilder().setAppAuth(appAuth)
                .setStatus(TaskStatus.SUCCESS.getValue()).setTaskId(taskId).build();

        ActorSelection serverActor = system.actorSelection(actorPath);
        try {
            response = (ServerModifyTaskStatusResponse) FutureUtils.awaitResult(serverActor, request, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(response.getSuccess());
        assertEquals(TaskStatus.SUCCESS.getValue(), (int) taskService.get(taskId).getStatus());
    }

    //@Test
    public void testKillTask() {
        KillTaskProtos.RestServerKillTaskRequest request = null;
        KillTaskProtos.ServerKillTaskResponse response = null;
        ActorSelection serverActor = system.actorSelection(actorPath);
        List<Integer> statusList = Lists.newArrayList();
        statusList.add(TaskStatus.READY.getValue());
        //        statusList.add(TaskStatus.RUNNING.getValue());
        long killTaskId = getRandomTaskId(statusList);
        Task killTask = taskService.get(killTaskId);

        request = KillTaskProtos.RestServerKillTaskRequest.newBuilder().setAppAuth(appAuth).addTaskId(killTaskId).build();
        try {
            response = (KillTaskProtos.ServerKillTaskResponse) FutureUtils.awaitResult(serverActor, request, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(response.getSuccess());

    }

    public ActorSystem getActorSystem() {
        if (system == null) {
            Config akkaConfig = ConfigUtils.getAkkaConfig("akka-test.conf");
            system = ActorSystem.create(TestJarvisConstants.TEST_AKKA_SYSTEM_NAME, akkaConfig);
        }

        return system;
    }

    @Ignore
    @Test
    public void submitRunningJobMakePlanTask() {
        AppAuth appAuth = AppAuth.newBuilder().setToken("11111").setName("jarvis-web").build();
        ActorSelection serverActor = system.actorSelection(actorPath);
        String timeExpression = "R1/" + DateTime.now().plusMinutes(1).toString() + "/PT1H";
        List<ScheduleExpressionEntry> expressionEntries = Lists.newArrayList();
        //添加时间依赖
        ScheduleExpressionEntry expressionEntry = ScheduleExpressionEntry.newBuilder().setExpressionType(ScheduleExpressionType.ISO8601.getValue())
                .setOperator(OperationMode.ADD.getValue()).setScheduleExpression(timeExpression).setExpressionId(35L).build();

        expressionEntries.add(expressionEntry);

        RestSubmitJobRequest request = RestSubmitJobRequest.newBuilder().setJobName("qh_test").setAppName("jarvis-web").setAppAuth(appAuth)
                .setContent("show databases;").setPriority(JobPriority.HIGH.getValue()).setParameters("{\"para1\":\"1\",\"para2\":\"2\"}")
                .setStatus(JobStatus.ENABLE.getValue()).setUser("qinghuo").addAllExpressionEntry(expressionEntries).setExpiredTime(86400)
                .setFailedAttempts(3).setFailedInterval(3).setJobType("hive").setWorkerGroupId(1).build();
        ServerSubmitJobResponse response = null;
        try {
            response = (ServerSubmitJobResponse) FutureUtils.awaitResult(serverActor, request, 30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long newJobid = response.getJobId();

        DateTime now = DateTime.now();

        //进行验证
        new CheckTaskRunningService(newJobid, now, 2).run();

        JobService jobService = Injectors4Test.getInjector().getInstance(JobService.class);
        JobScheduleExpression scheduleExpression = jobService.getScheduleExpressionByJobId(newJobid);
        long scheduleExpId = scheduleExpression.getId();

        //删掉时间依赖
        expressionEntry = ScheduleExpressionEntry.newBuilder().setExpressionType(ScheduleExpressionType.ISO8601.getValue())
                .setOperator(OperationMode.DELETE.getValue()).setExpressionId(scheduleExpId).build();

        List<ScheduleExpressionEntry> removeEntries = Lists.newArrayList();
        removeEntries.add(expressionEntry);
        RestModifyJobScheduleExpRequest removeRequest = RestModifyJobScheduleExpRequest.newBuilder().setUser("qinghuo").setAppAuth(appAuth)
                .setJobId(newJobid).addAllExpressionEntry(removeEntries).build();

        try {
            FutureUtils.awaitResult(serverActor, removeRequest, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //先把job设置为不可用，从内存里删除
        JobProtos.RestModifyJobStatusRequest removeJobRequest = JobProtos.RestModifyJobStatusRequest.newBuilder().setAppAuth(appAuth)
                .setStatus(JobStatus.DELETED.getValue()).setUser("qinghuo").addJobId(newJobid).build();
        try {
            FutureUtils.awaitResult(serverActor, removeJobRequest, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        //删掉job本身
        //        removeJobRequest = JobProtos.RestModifyJobStatusRequest.newBuilder()
        //                .setAppAuth(appAuth)
        //                .setStatus(JobStatus.DELETED.getValue())
        //                .setUser("qinghuo")
        //                .setJobId(response.getJobId())
        //                .build();
        //        try {
        //            FutureUtils.awaitResult(serverActor, removeJobRequest, 15);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }

    }

    public void testTmp() {
        long scheduleExpId = 73;
        long newJobid = 367L;
        AppAuth appAuth = AppAuth.newBuilder().setToken("11111").setName("jarvis-web").build();

        ActorSelection serverActor = system.actorSelection(actorPath);
        //删掉时间依赖
        ScheduleExpressionEntry expressionEntry = ScheduleExpressionEntry.newBuilder().setExpressionType(ScheduleExpressionType.ISO8601.getValue())
                .setOperator(OperationMode.DELETE.getValue()).setExpressionId(scheduleExpId).build();

        List<ScheduleExpressionEntry> removeEntries = Lists.newArrayList();
        removeEntries.add(expressionEntry);
        RestModifyJobScheduleExpRequest removeRequest = RestModifyJobScheduleExpRequest.newBuilder().setUser("qinghuo").setAppAuth(appAuth)
                .setJobId(newJobid).addAllExpressionEntry(removeEntries).build();

        try {
            FutureUtils.awaitResult(serverActor, removeRequest, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //删掉job本身
        RestRemoveJobRequest removeJobRequest = RestRemoveJobRequest.newBuilder().setUser("qinghuo").setAppAuth(appAuth).setJobId(newJobid).build();
        try {
            FutureUtils.awaitResult(serverActor, removeJobRequest, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveTask() {
        ActorSelection serverActor = system.actorSelection(actorPath);
        long taskid = 8274L;
        Task task = taskService.get(taskid);
        assertNotNull(task);
        RestServerRemoveTaskRequest request = RestServerRemoveTaskRequest.newBuilder().setAppAuth(appAuth).addTaskId(taskid).build();
        ServerRemoveTaskResponse response = null;
        try {
            response = (ServerRemoveTaskResponse) FutureUtils.awaitResult(serverActor, request, 30);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(response.getSuccess());
        Task taskNew = taskService.get(taskid);
        assertEquals(TaskStatus.REMOVED.getValue(), (int) taskNew.getStatus());

    }

    class CheckTaskRunningService implements Runnable {
        long jobId;
        DateTime dateTime;
        int offset;

        public CheckTaskRunningService(long jobId, DateTime dateTime, int offset) {
            this.jobId = jobId;
            this.dateTime = dateTime;
            this.offset = offset;
        }

        @Override
        public void run() {
            int flag = 0;
            while (true) {
                Range<DateTime> range = Range.closedOpen(dateTime.minusMinutes(3), dateTime.plusMinutes(4));
                List<Task> tasks = taskService.getTasksBetween(jobId, range);
                System.out.println(range.toString());
                for (Task task : tasks) {
                    if (task != null && (task.getStatus() == 2 || task.getStatus() == 3 || task.getStatus() == 4)) {

                        assertThat(task.getStatus(), allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(4)));
                        flag = 60;
                        break;
                    }
                }

                try {
                    flag++;
                    if (flag > 60)
                        break;
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
