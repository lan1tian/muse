/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月12日 下午8:33:04
 */

package com.mogujie.jarvis.worker.status.lookup;

import com.mogujie.jarvis.core.domain.TaskDetail;

public class HiveStatusLookup extends YarnStatusLookup {

    @Override
    public boolean accept(TaskDetail taskDetail, String appName) {
        return appName.equals(taskDetail.getTaskName());
    }

}
