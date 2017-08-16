/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月14日 下午2:46:52
 */

package com.mogujie.jarvis.core.metrics;

import org.apache.commons.configuration.Configuration;

import com.codahale.metrics.MetricRegistry;

public interface MetricSource {

    void getMetric(MetricRegistry registry, Configuration conf);
}
