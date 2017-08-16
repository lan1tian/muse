/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月15日 下午5:09:29
 */

package com.mogujie.jarvis.server.dispatcher;

import com.mogujie.jarvis.core.domain.TaskDetail;

public interface TaskQueue {

    void put(TaskDetail taskDetail);

    void removeByKey(String key);

    TaskDetail get();

    int size();

    boolean isEmpty();

    void clear();
}
