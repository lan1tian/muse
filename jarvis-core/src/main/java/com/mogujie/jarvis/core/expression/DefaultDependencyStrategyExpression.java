/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月6日 上午11:00:15
 */

package com.mogujie.jarvis.core.expression;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides a parser and evaluator for dependency strategy expressions, such as "L(1)".
 * Expression format: "*", "+", "L(n)"
 */
public class DefaultDependencyStrategyExpression extends DependencyStrategyExpression {

    private int isValid;
    private int lastValue;
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\*|\\+|L\\((\\d+)\\)");

    public DefaultDependencyStrategyExpression(String expression) {
        super(expression);
    }

    @Override
    public boolean isValid() {
        Matcher m = EXPRESSION_PATTERN.matcher(expression);
        if (m.matches()) {
            if (expression.startsWith("L")) {
                lastValue = Integer.parseInt(m.group(1));
                if (lastValue < 1) {
                    isValid = -1;
                    return false;
                }
            }

            isValid = 1;
            return true;
        }

        isValid = -1;
        return false;
    }

    @Override
    public boolean check(List<Boolean> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }

        if (isValid > 0 || (isValid == 0 && isValid())) {
            char c = expression.charAt(0);
            switch (c) {
                case '*':
                    for (Boolean status : list) {
                        if (!status) {
                            return false;
                        }
                    }
                    return true;
                case '+':
                    for (Boolean status : list) {
                        if (status) {
                            return true;
                        }
                    }
                    return false;
                case 'L':
                    for (int len = list.size(), i = len - lastValue; i < len; i++) {
                        if (!list.get(i)) {
                            return false;
                        }
                    }
                    return true;
                default:
                    break;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return expression;
    }

}
