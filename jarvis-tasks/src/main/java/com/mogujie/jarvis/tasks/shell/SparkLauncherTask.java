/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2016年1月4日 下午3:44:48
 */

package com.mogujie.jarvis.tasks.shell;
import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.util.JsonHelper;
import java.util.Map;

public class SparkLauncherTask extends ShellTask {

    public SparkLauncherTask(TaskContext taskContext) {
        super(taskContext);
    }

    @Override
    public String getCommand() {
        Map<String, Object> parameters = getTaskContext().getTaskDetail().getParameters();
        parameters.put("taskName",getTaskContext().getTaskDetail().getTaskName());
        return "sparkLauncher.sh \""  +  JsonHelper.toJson(parameters).replaceAll("\"","\\\\\"") +"\"";
    }

}
