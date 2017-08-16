/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月29日 上午11:09:46
 */

package com.mogujie.jarvis.core.util;

import com.mogujie.jarvis.core.domain.IdType;

public class IdUtils {

    public static long parse(String fullId, IdType type) {
        String[] tokens = fullId.split("_", 3);
        switch (type) {
            case JOB_ID:
                return Long.parseLong(tokens[0]);
            case TASK_ID:
                return Long.parseLong(tokens[1]);
            case ATTEMPT_ID:
            default:
                return Long.parseLong(tokens[2]);
        }
    }

    public static String getFullId(long jobId, long taskId, int attemptId) {
        return jobId + "_" + taskId + "_" + attemptId;
    }

    public static String getLogId(long taskId, int attemptId) {
        return taskId + "_" + attemptId;
    }

    public static String getLogIdFromFullId(String fullId){
        String[] tokens = fullId.split("_", 3);
        return  tokens[1] + "_" + tokens[2];
    }

}
