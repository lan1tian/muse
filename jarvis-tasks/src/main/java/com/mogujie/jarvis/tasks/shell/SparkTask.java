/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月4日 下午3:44:48
 */

package com.mogujie.jarvis.tasks.shell;

import java.util.List;

import com.google.common.base.Joiner;
import com.mogujie.jarvis.core.TaskContext;

public class SparkTask extends JavaTask {

    public SparkTask(TaskContext taskContext) {
        super(taskContext);
    }

    @Override
    protected String getCmd(String jar, List<String> classpath, String mainClass, String args) {
        String jars = classpath.size() == 0 ? "" : " --jars " + Joiner.on(",").join(classpath);
        return "spark-submit " + jar + jars + " --class " + mainClass + " " + args;
    }
}
