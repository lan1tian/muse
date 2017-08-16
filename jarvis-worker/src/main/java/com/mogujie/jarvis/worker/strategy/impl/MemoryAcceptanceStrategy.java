/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午7:57:49
 */

package com.mogujie.jarvis.worker.strategy.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.io.Files;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.worker.WorkerConfigKeys;
import com.mogujie.jarvis.worker.strategy.AcceptanceResult;
import com.mogujie.jarvis.worker.strategy.AcceptanceStrategy;

/**
 * @author wuya
 *
 */
public class MemoryAcceptanceStrategy implements AcceptanceStrategy {

    private static final String DECIMAL_FORMAT = "#0.00";
    private static final double MAX_MEMORY_USAGE = ConfigUtils.getWorkerConfig().getDouble(WorkerConfigKeys.WORKER_MEMORY_USAGE_THRESHOLD, 0.9);

    @Override
    public AcceptanceResult accept(TaskDetail taskDetail) throws Exception {
        try {
            List<String> lines = Files.readLines(new File("/proc/meminfo"), StandardCharsets.UTF_8);
            long memTotal = Long.parseLong(CharMatcher.DIGIT.retainFrom(lines.get(0)));
            long memFree = Long.parseLong(CharMatcher.DIGIT.retainFrom(lines.get(1)));
            long buffers = Long.parseLong(CharMatcher.DIGIT.retainFrom(lines.get(2)));
            long cached = Long.parseLong(CharMatcher.DIGIT.retainFrom(lines.get(3)));
            double currentMemoryUsage = (memTotal - memFree - buffers - cached) / (double) memTotal;
            if (Double.isNaN(currentMemoryUsage)) {
                currentMemoryUsage = 0;
            }
            if (currentMemoryUsage > MAX_MEMORY_USAGE) {
                return new AcceptanceResult(false,
                        "client当前内存使用率" + new DecimalFormat(DECIMAL_FORMAT).format(currentMemoryUsage) + ", 超过阈值" + MAX_MEMORY_USAGE);
            }
        } catch (IOException e) {
            return new AcceptanceResult(false, e.getMessage());
        }

        return new AcceptanceResult(true, "");
    }

}
