/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午7:50:37
 */

package com.mogujie.jarvis.worker.strategy;

import com.mogujie.jarvis.core.domain.TaskDetail;

/**
 * @author wuya
 *
 */
public interface AcceptanceStrategy {

    AcceptanceResult accept(TaskDetail taskDetail) throws Exception;
}
