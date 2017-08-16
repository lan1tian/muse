/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月14日 下午2:49:33
 */

package com.mogujie.jarvis.core.metrics;

import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.google.common.base.Throwables;

public class Metrics {

    public static void start(Configuration conf) {
        MetricRegistry registry = new MetricRegistry();
        ScheduledReporter reporter = MetricReporterFactory.createReporter(registry, conf);
        if (reporter != null) {
            reporter.start(conf.getInt("metrics.report.period.seconds", 60), TimeUnit.SECONDS);
            String[] metricSourceClassNameArray = conf.getStringArray("metrics.source.list");
            for (String className : metricSourceClassNameArray) {
                try {
                    MetricSource metricSource = (MetricSource) Class.forName(className).newInstance();
                    metricSource.getMetric(registry, conf);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    Throwables.propagate(e);
                }
            }
        }
    }
}
