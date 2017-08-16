/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月12日 下午7:14:18
 */
package com.mogujie.jarvis.core.util;

import com.google.common.base.Preconditions;
import com.mogujie.jarvis.core.expression.CronExpression;
import com.mogujie.jarvis.core.expression.FixedDelayExpression;
import com.mogujie.jarvis.core.expression.FixedRateExpression;
import com.mogujie.jarvis.core.expression.ISO8601Expression;
import com.mogujie.jarvis.core.expression.ScheduleExpression;
import com.mogujie.jarvis.core.expression.ScheduleExpressionType;

public class ExpressionUtils {

    public static void checkExpression(Integer expressionType, String expressionStr) throws IllegalArgumentException {

        Preconditions.checkArgument(expressionType != null, "scheduleExpressionType不能为空");
        Preconditions.checkArgument(expressionStr != null, "scheduleExpression表达式不能为空");

        Preconditions.checkArgument(ScheduleExpressionType.isValid(expressionType), "scheduleExpressionType不对。type:" + expressionType);

        ScheduleExpression scheduleExpression = null;
        if (expressionType == ScheduleExpressionType.CRON.getValue()) {
            scheduleExpression = new CronExpression(expressionStr);
        } else if (expressionType == ScheduleExpressionType.FIXED_RATE.getValue()) {
            scheduleExpression = new FixedRateExpression(expressionStr);
        } else if (expressionType == ScheduleExpressionType.FIXED_DELAY.getValue()) {
            scheduleExpression = new FixedDelayExpression(expressionStr);
        } else if (expressionType == ScheduleExpressionType.ISO8601.getValue()) {
            scheduleExpression = new ISO8601Expression(expressionStr);
        }

        Preconditions.checkNotNull(scheduleExpression, "scheduleExpression不能为空");
        Preconditions.checkArgument(scheduleExpression.isValid(), "scheduleExpression表达式不对 exp:" + expressionStr);

    }
}
