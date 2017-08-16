/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午3:50:15
 */

package com.mogujie.jarvis.server.dispatcher.workerselector;

import com.mogujie.jarvis.core.domain.WorkerInfo;

/**
 * @author wuya
 *
 */
public interface WorkerSelector {

    WorkerInfo select(int workerGroupId);
}
