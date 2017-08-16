/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月6日 下午5:04:28
 */

package com.mogujie.jarvis.worker.status;

import org.apache.commons.configuration.Configuration;

import com.mogujie.jarvis.core.domain.TaskDetail;

public interface TaskStatusLookup {

    void init(Configuration conf);

    int lookup(TaskDetail taskDetail);

    void close();

}
