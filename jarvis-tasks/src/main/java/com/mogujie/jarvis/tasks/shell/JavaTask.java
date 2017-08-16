/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月28日 下午4:43:25
 */

package com.mogujie.jarvis.tasks.shell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.domain.IdType;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.exception.TaskException;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.core.util.IdUtils;

public class JavaTask extends ShellTask {

    private TaskDetail taskDetail;
    private String hdfsDir;
    private String localDir;

    private String mainClass;
    private String args;
    private String jar;
    private String classpath;

    private static final String HDFS_ROOT_PATH = ConfigUtils.getWorkerConfig().getString("hdfs.jar.root.path");
    private static final String LOCAL_ROOT_PATH = ConfigUtils.getWorkerConfig().getString("local.jar.root.path", "/tmp/jarvis/tasks/java");
    private static final Logger LOGGER = LogManager.getLogger();

    public JavaTask(TaskContext taskContext) {
        super(taskContext);
        taskDetail = getTaskContext().getTaskDetail();
        long jobId = IdUtils.parse(taskDetail.getFullId(), IdType.JOB_ID);
        hdfsDir = HDFS_ROOT_PATH + "/" + taskDetail.getUser() + "/" + jobId;
        localDir = LOCAL_ROOT_PATH + "/" + taskDetail.getUser() + "/" + jobId;

        Map<String, Object> parameters = taskDetail.getParameters();
        mainClass = parameters.get("mainClass").toString();
        args = parameters.get("arguments").toString();
        jar = parameters.get("jar").toString();
        classpath = parameters.get("classpath").toString();
    }

    private void downloadJarFromHDFS(String filename) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        File localFile = new File(localDir + "/" + filename);
//        Path p1 = new Path("hdfs://" + hdfsDir + "/" + filename);
        Path p1 = new Path(filename);
        Path p2 = new Path("file://" + localFile.getAbsolutePath());

        if (!fs.exists(p1)) {
            throw new FileNotFoundException(p1.toString());
        }

        long hdfsJarModificationTime = fs.getFileLinkStatus(p1).getModificationTime();
        FileUtils.forceMkdir(new File(localDir).getParentFile());

        if (!localFile.exists() || hdfsJarModificationTime > localFile.lastModified()) {
            try {
                fs.copyToLocalFile(p1, p2);
            } catch (IOException e) {
                fs.deleteOnExit(p2);
            }

            boolean result = localFile.setLastModified(hdfsJarModificationTime);
            if (!result) {
                LOGGER.error("File [{}] modified failed", localFile.getPath());
            }
            fs.close();
        }
    }

    @Override
    public void preExecute() throws TaskException {
        try {
            downloadJarFromHDFS(jar);
            List<String> cps = Lists.newArrayList(classpath.split(","));
            for (String cp : cps) {
                if (!cp.trim().isEmpty()) {
                    downloadJarFromHDFS(cp);
                }
            }

            File file = new File(localDir);
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        String filename = f.getName();
                        if (!filename.equals(jar) && !cps.contains(filename)) {
                            FileUtils.forceDeleteOnExit(new File(localDir + "/" + filename));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new TaskException(e);
        }
    }

    protected String getCmd(String jar, List<String> classpath, String mainClass, String args) {
        classpath.add(jar);
        return "java -cp " + Joiner.on(":").join(classpath) + " " + mainClass + " " + args;
    }

    @Override
    public String getCommand() {
        List<String> list = Lists.newArrayList();
        for (String cp : classpath.split(",")) {
            list.add(localDir + "/" + cp);
        }

        return getCmd(localDir + "/" + jar, list, mainClass, args);
    }

}
