/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月15日 下午3:04:10
 */

package com.mogujie.jarvis.core.metrics;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Assert;
import org.junit.Test;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.ganglia.GangliaReporter;

public class TestMetricReporterFactory {

    private MetricRegistry metricRegistry = new MetricRegistry();
    private Configuration conf = new PropertiesConfiguration();

    @Test
    public void testCreateCSVReporter() {
        conf.addProperty("metrics.report.type", "csv");
        conf.addProperty("metrics.report.csv.dir", System.getProperty("java.io.tmpdir"));
        ScheduledReporter scheduledReporter = MetricReporterFactory.createReporter(metricRegistry, conf);
        Assert.assertTrue(scheduledReporter instanceof CsvReporter);
    }

    @Test
    public void testCreateGangliaReporter() {
        conf.clear();
        conf.addProperty("metrics.report.type", "ganglia");
        conf.addProperty("metrics.report.ganglia.group", "localhost");
        ScheduledReporter scheduledReporter = MetricReporterFactory.createReporter(metricRegistry, conf);
        Assert.assertTrue(scheduledReporter instanceof GangliaReporter);
    }

    @Test
    public void testCreateConsoleReporter() {
        conf.clear();
        conf.addProperty("metrics.report.type", "console");
        ScheduledReporter scheduledReporter = MetricReporterFactory.createReporter(metricRegistry, conf);
        Assert.assertTrue(scheduledReporter instanceof ConsoleReporter);
    }

}
