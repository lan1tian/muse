/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月14日 下午2:16:30
 */

package com.mogujie.jarvis.core.metrics;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.Configuration;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.ganglia.GangliaReporter;
import com.google.common.base.Throwables;

import info.ganglia.gmetric4j.gmetric.GMetric;
import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;

public class MetricReporterFactory {

    public static ScheduledReporter createReporter(MetricRegistry registry, Configuration conf) {
        switch (conf.getString("metrics.report.type", "console").toLowerCase()) {
            case "csv":
                return CsvReporter.forRegistry(registry).build(new File(conf.getString("metrics.report.csv.dir")));
            case "ganglia":
                String group = conf.getString("metrics.report.ganglia.group");
                int port = conf.getInt("metrics.report.ganglia.port", 8649);
                UDPAddressingMode mode = UDPAddressingMode.valueOf(conf.getString("metrics.report.ganglia.mode", "MULTICAST").toUpperCase());
                int ttl = conf.getInt("metrics.report.ganglia.ttl", 1);
                try {
                    GMetric gmetric = new GMetric(group, port, mode, ttl);
                    return GangliaReporter.forRegistry(registry).build(gmetric);
                } catch (IOException e) {
                    Throwables.propagate(e);
                    return null;
                }
            default:
                return ConsoleReporter.forRegistry(registry).build();
        }
    }
}
