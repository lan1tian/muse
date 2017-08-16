package com.mogujie.jarvis.worker.actor;

import org.junit.Test;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.worker.util.TaskConfigUtils;

/**
 * Created by muming on 15/12/1.
 */
public class TestWorker {

    @Test
    public void testJobXml() throws UnirestException {
        TaskConfigUtils.getRegisteredTasks().get("dummy");
    }
}
