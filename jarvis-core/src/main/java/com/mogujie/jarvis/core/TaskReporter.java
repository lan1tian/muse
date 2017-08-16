/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年10月9日 上午9:58:47
 */

package com.mogujie.jarvis.core;

import com.mogujie.jarvis.core.domain.TaskDetail;

public interface TaskReporter {

    void report(TaskDetail taskDetail);

}
