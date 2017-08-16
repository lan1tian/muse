package com.mogujie.jarvis.core.domain;

/**
 * @author guangming
 *
 */
public enum CommonStrategy {
    ALL(1, "*","全部成功"),        // 依赖全部成功
    LASTONE(2, "L(1)","最后一次成功"),    // 依赖最后一次成功
    ANYONE(3, "+","任何一次成功");     // 依赖任何一次成功

    private int value;
    private String expression;
    private String description;


    CommonStrategy(int value, String expression,String description) {
        this.value = value;
        this.expression = expression;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getExpression() {
        return expression;
    }

    public String getDescription() {
        return description;
    }

    public static CommonStrategy getInstance(int value) {
        CommonStrategy[] strategyList = CommonStrategy.values();
        CommonStrategy strategy = CommonStrategy.ALL;
        for (CommonStrategy cs : strategyList) {
            if (cs.getValue() == value) {
                strategy = cs;
                break;
            }
        }
        return strategy;
    }

    public static CommonStrategy getInstance(String expression) {
        CommonStrategy[] strategyList = CommonStrategy.values();
        CommonStrategy strategy = CommonStrategy.ALL;
        for (CommonStrategy cs : strategyList) {
            if (cs.getExpression().equalsIgnoreCase(expression)) {
                strategy = cs;
                break;
            }
        }
        return strategy;
    }

    public static Boolean isValid(int value) {
        CommonStrategy[] values = CommonStrategy.values();
        for (CommonStrategy s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

}