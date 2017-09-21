/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月9日 下午4:34:01
 */

package com.mogujie.jarvis.tasks.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogujie.jarvis.core.AbstractTask;
import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.domain.StreamType;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.exception.TaskException;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.tasks.ShellStreamProcessor;
import com.mogujie.jarvis.tasks.util.HiveScriptParamUtils;
import com.mogujie.jarvis.tasks.util.ShellUtils;

/**
 * @author muming
 *
 */
public class ShellTask extends AbstractTask {

    private Process shellProcess = null;
    private static final String DEFAULT_STATUS_PATH = "/tmp/jarvis_shell_status";
    private static final String STATUS_PATH_KEY = "shell.status.data.dir";
    private static final String STATUS_PATH = ConfigUtils.getWorkerConfig().getString(STATUS_PATH_KEY, DEFAULT_STATUS_PATH);
    private static final Logger LOGGER = LogManager.getLogger();

    public ShellTask(TaskContext taskContext) {
        super(taskContext);
        File file = new File(STATUS_PATH);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                LOGGER.error("Can't mkdir: {}", file.getAbsolutePath());
            }
        }
    }

    public String getCommand() {
        return getTaskContext().getTaskDetail().getContent();
    }

    public void processStdOutputStream(InputStream inputStream) {
        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                getTaskContext().getLogCollector().collectStdout(line);
            }
        } catch (IOException e) {
            LOGGER.error("error shellProcess stdout stream", e);
        } finally {
            getTaskContext().getLogCollector().collectStdout("", true);
        }
    }

    public void processStdErrorStream(InputStream inputStream) {
        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                getTaskContext().getLogCollector().collectStderr(line);
            }
        } catch (IOException e) {
            LOGGER.error("error shellProcess stderr stream", e);
        } finally {
            getTaskContext().getLogCollector().collectStderr("", true);
        }
    }

    @Override
    public void postExecute() throws TaskException {
        super.postExecute();

        //task有变化情况下, 返回新的执行内容和user
        String newContent = getCommand();
        TaskDetail oldTaskDetail = getTaskContext().getTaskDetail();
        if (!newContent.equals(oldTaskDetail.getContent()) || oldTaskDetail.isChanged()) {
            LOGGER.debug("shellTask.postExecute() update newContent and newUser");
            TaskDetail newTaskDetail = TaskDetail.newTaskDetailBuilder(oldTaskDetail).setContent(newContent).setUser(oldTaskDetail.getUser()).build();
            getTaskContext().getTaskReporter().report(newTaskDetail);
        }

    }

    @Override
    public boolean execute() {
        TaskDetail task = getTaskContext().getTaskDetail();
        String statusFilePath = STATUS_PATH + "/" + task.getFullId() + ".status";
        try {
            StringBuilder sb = new StringBuilder();
            LOGGER.info("pre execute taskId:"+task.getFullId()+", cmd:"+getCommand()+", datatime:"+task.getDataTime());
            String cmd = HiveScriptParamUtils.parse(getCommand(), task.getDataTime());
            LOGGER.info("post execute taskId:"+task.getFullId()+",  cmd:"+cmd+", datatime:"+task.getDataTime());
            sb.append("(");
            sb.append(cmd);
            sb.append(");export JARVIS_EXIT_CODE=$? ").append("&& echo $JARVIS_EXIT_CODE > ").append(statusFilePath)
                    .append(" && exit $JARVIS_EXIT_CODE");

            ProcessBuilder processBuilder = ShellUtils.createProcessBuilder(sb.toString());
            if (redirectStream()) {
                processBuilder.redirectErrorStream(true);
            }
            shellProcess = processBuilder.start();

            CountDownLatch countDownLatch = null;
            if (redirectStream()) {
                countDownLatch = new CountDownLatch(1);
                Thread stderrStreamProcessor = new ShellStreamProcessor(this, shellProcess.getInputStream(), StreamType.STD_ERR, countDownLatch);
                stderrStreamProcessor.start();
            } else {
                countDownLatch = new CountDownLatch(2);
                Thread stdoutStreamProcessor = new ShellStreamProcessor(this, shellProcess.getInputStream(), StreamType.STD_OUT, countDownLatch);
                stdoutStreamProcessor.start();

                Thread stderrStreamProcessor = new ShellStreamProcessor(this, shellProcess.getErrorStream(), StreamType.STD_ERR, countDownLatch);
                stderrStreamProcessor.start();
            }

            boolean result = (shellProcess.waitFor() == 0);
            countDownLatch.await();
            return result;
        } catch (Exception e) {
            getTaskContext().getLogCollector().collectStderr(e.getMessage());
            return false;
        } finally {
            // 删除状态文件
            File statusFile = new File(statusFilePath);
            if (statusFile.exists()) {
                boolean deleted = statusFile.delete();
                if (!deleted) {
                    LOGGER.error("File [" + statusFilePath + "] delete failed");
                }
            }
        }
    }

    @Override
    public boolean kill() {
        if (shellProcess != null) {
            shellProcess.destroy();
        }
        return true;
    }

    public boolean redirectStream() {
        return true;
    }

}
