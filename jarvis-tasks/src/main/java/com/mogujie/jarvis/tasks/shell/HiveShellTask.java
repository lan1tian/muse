/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月10日 上午9:21:13
 */

package com.mogujie.jarvis.tasks.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.exception.ShellException;
import com.mogujie.jarvis.core.exception.TaskException;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.tasks.domain.HiveTaskEntity;
import com.mogujie.jarvis.tasks.util.HiveConfigUtils;
import com.mogujie.jarvis.tasks.util.HiveScriptParamUtils;
import com.mogujie.jarvis.tasks.util.MoguAnnotationUtils;
import com.mogujie.jarvis.tasks.util.YarnUtils;

/**
 * @author wuya,muming
 */
public class HiveShellTask extends ShellTask {

    private Set<String> applicationIdSet = new HashSet<>();
    private static final Pattern APPLICATION_ID_PATTERN = Pattern.compile("application_\\d+_\\d+");
    private static final Pattern MAPPERS_NUMBER_PATTERN = Pattern.compile("number of mappers: (\\d+);");

    private static final Logger LOGGER = LogManager.getLogger();

    public HiveShellTask(TaskContext jobContext) {
        super(jobContext);
    }

    @Override
    public void preExecute() throws TaskException {
        TaskDetail task = getTaskContext().getTaskDetail();
        // 更新user
        HiveTaskEntity entity = HiveConfigUtils.getHiveJobEntry(task.getAppName());
        if (entity != null && task.getUser().isEmpty()) {
            task.setUser(entity.getUser());
            task.setChanged(true);
            LOGGER.info("更新task[{}] user from {} to {}", task.getFullId(), task.getUser(), entity.getUser());
        }
    }

    //bug通过文本重跑的命令有问题，每次重跑会叠加执行内容，暂时没有fix
    @Override
    public String getCommand() {
        TaskDetail task = getTaskContext().getTaskDetail();
        String user = task.getUser();

        Configuration workerConfig = ConfigUtils.getWorkerConfig();
        boolean isHive2Enable = workerConfig.getBoolean("hive2.enable", false);

        StringBuilder sb = new StringBuilder();
        sb.append("export HADOOP_USER_NAME=" + PinyinHelper.convertToPinyinString(user.trim(), "", PinyinFormat.WITHOUT_TONE) + ";");
        if (isHive2Enable) {
            String hive2Host = workerConfig.getString("hive2.host");
            sb.append("beeline --outputformat=tsv2 -u jdbc:hive2://" + hive2Host + " -n " + user);
        } else {
            sb.append("hive");
        }

        sb.append(" -e \"set hive.cli.print.header=true;");
        sb.append("set mapred.job.name=" + YarnUtils.getYarnJob(task) + ";");
        // 打印列名的时候不打印表名，否则xray无法显示数据
        sb.append("set hive.resultset.use.unique.column.names=false;");
        sb.append(MoguAnnotationUtils
                .removeAnnotation(HiveScriptParamUtils.parse(replaceTmpTableName(getContent(task), task.getDataTime()), task.getDataTime())));
        sb.append("\"");
        LOGGER.info("command="+sb);
        return sb.toString();
    }

    @Override
    public void processStdOutputStream(InputStream inputStream) {
        TaskDetail task = getTaskContext().getTaskDetail();
        int currentResultRows = 0;
        int maxResultRows = 10000;
        String appName = task.getAppName();
        HiveTaskEntity entry = HiveConfigUtils.getHiveJobEntry(appName);
        if (entry != null) {
            maxResultRows = entry.getMaxResultRows();
        }

        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                currentResultRows++;
                if (maxResultRows >= 0 && currentResultRows > maxResultRows) {
                    break;
                }
                getTaskContext().getLogCollector().collectStdout(line);
            }
            getTaskContext().getLogCollector().collectStdout("");
        } catch (IOException e) {
            LOGGER.error("error process stdouput stream", e);
        }
    }

    @Override
    public void processStdErrorStream(InputStream inputStream) {
        TaskDetail task = getTaskContext().getTaskDetail();
        int maxMapperNum = 2000;
        String appName = task.getAppName();
        HiveTaskEntity entry = HiveConfigUtils.getHiveJobEntry(appName);
        if (entry != null) {
            maxMapperNum = entry.getMaxMapperNum();
        }

        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            while ((line = br.readLine()) != null) {
                getTaskContext().getLogCollector().collectStderr(line);

                // 从输出日志中提取application id
                String applicationId = match(APPLICATION_ID_PATTERN, 0, line);
                if (applicationId != null) {
                    applicationIdSet.add(applicationId);
                }

                // 检查map数是否超过限制，超过则kill掉任务
                String mappersNum = match(MAPPERS_NUMBER_PATTERN, 1, line);
                if (mappersNum != null) {
                    int num = Integer.parseInt(mappersNum);
                    if (maxMapperNum >= 0 && num > maxMapperNum) {
                        kill();
                        getTaskContext().getLogCollector().collectStderr("Job已被Kill，Map数量(" + num + ")超过(" + maxMapperNum + ")限制");
                        break;
                    }
                }
            }
            getTaskContext().getLogCollector().collectStderr("");
        } catch (IOException e) {
            LOGGER.error("error process stderr stream", e);
        }
    }

    @Override
    public boolean kill() {

        boolean result = true;
        // kill掉yarn application
        try {
            YarnUtils.killApplicationByIds(applicationIdSet);
        } catch (ShellException e) {
            result = false;
        }

        result &= super.kill();
        return result;
    }

    /**
     * 替换临时表变量: $tmptable -> xray${yyyyMMdd}
     * @param hql
     * @param dataTime
     * @return
     */
    private String replaceTmpTableName(String hql, DateTime dataTime) {
        return hql.replace("$tmptable", "xray" + dataTime.toString("yyyyMMdd"));
    }

    private String match(Pattern pattern, int group, String line) {
        Matcher m = pattern.matcher(line);
        if (m.find()) {
            return m.group(group);
        }

        return null;
    }

    protected String getContent(TaskDetail task) {
        return task.getContent();
    }

    @Override
    public boolean redirectStream() {
        return false;
    }

}
