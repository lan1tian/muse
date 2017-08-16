/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午7:57:49
 */

package com.mogujie.jarvis.worker.strategy.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;

import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.worker.WorkerConfigKeys;
import com.mogujie.jarvis.worker.strategy.AcceptanceResult;
import com.mogujie.jarvis.worker.strategy.AcceptanceStrategy;

/**
 * @author wuya
 *
 */
public class LoadAcceptanceStrategy implements AcceptanceStrategy {

    private static final String DECIMAL_FORMAT = "#0.00";
    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();
    public static final double LOAD_THRESHOLD = ConfigUtils.getWorkerConfig().getDouble(WorkerConfigKeys.WORKER_CPU_LOAD_AVG_THRESHOLD,
            CPU_NUM * 1.5);

    @Override
    public AcceptanceResult accept(TaskDetail taskDetail) throws Exception {
        OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        double currentLoad = bean.getSystemLoadAverage();
        if (Double.isNaN(currentLoad)) {
            currentLoad = 0;
        }

        if (currentLoad > LOAD_THRESHOLD) {
            return new AcceptanceResult(false,
                    "client当前CPU Load " + new DecimalFormat(DECIMAL_FORMAT).format(currentLoad) + ", 超过阈值" + LOAD_THRESHOLD);
        }

        return new AcceptanceResult(true, "");
    }

}
