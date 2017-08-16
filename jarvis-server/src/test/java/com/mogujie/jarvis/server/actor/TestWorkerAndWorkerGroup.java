package com.mogujie.jarvis.server.actor;

import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.server.actor.base.TestWorkerServiceBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/1/11.
 * used by jarvis-parent
 */

public class TestWorkerAndWorkerGroup extends TestWorkerServiceBase {

    int flag;
    //    ActorSystem system = ActorSystem.create(JarvisConstants.WORKER_AKKA_SYSTEM_NAME, akkaConfig);


    @Before
    public void ready() {
        flag = 0;
    }

    @Test
    public void testWorkerRegistry() {
        WorkerInfo workerInfo = new WorkerInfo("127.0.0.1", 8888);
        WorkerInfo workerInfo1 = new WorkerInfo("192.168.0.1", 8889);
        WorkerInfo workerInfo2 = new WorkerInfo("10.11.129.58", 10000);
        workerRegistry.put(workerInfo, 1);
        workerRegistry.put(workerInfo1, 2);
        workerRegistry.put(workerInfo2, 3);
        Assert.assertEquals(workerRegistry.getWorkerGroupId(workerInfo), 1);
        Assert.assertEquals(workerRegistry.getWorkerGroupId(workerInfo1), 2);
        Assert.assertEquals(workerRegistry.getWorkerGroupId(workerInfo2), 3);
    }

    @Test
    public void testAddWorkerGroup() {
        Assert.assertEquals(workerGroupService.insert(workerGroup), 0);
    }

    @Test
    public void testAddWorker() {
        try {
            when(workerMapper, "insert", any()).thenAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    flag++;
                    resultList.add(flag);
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        workerService.saveWorker("129.0.0.1", 8888, 2);
        Assert.assertEquals((int) resultList.get(0), 1);

    }

    @Test
    public void testUpdateWorker() {
        try {
            when(workerMapper, "updateByPrimaryKeySelective", any()).then(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    flag++;
                    resultList.add(flag);
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        workerService.updateWorkerStatus(1, 2);
        Assert.assertEquals((int) resultList.get(0), 1);
    }

    @Test
    public void testUpdateWorkerGroup() {

        when(workerGroupMapper.updateByPrimaryKeySelective(workerGroup)).thenReturn(1);
        Assert.assertEquals(workerGroupService.update(workerGroup), 1);

    }
}
