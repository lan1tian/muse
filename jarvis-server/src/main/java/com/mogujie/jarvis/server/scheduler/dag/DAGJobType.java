/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月28日 下午4:31:12
 */

package com.mogujie.jarvis.server.scheduler.dag;

/**
 * @author guangming
 *
 */
public enum DAGJobType {
    NONE("---"),          // 立即执行任务？
    TIME("--t"),          // 定时任务
    DEPEND("-d-"),        // 依赖任务
    DEPEND_TIME("-dt"),   // 定时+依赖任务
    CYCLE("c--"),         // 循环任务（固定延时）
    CYCLE_TIME("c-t"),    // not supported
    CYCLE_DEPEND("cd-"),  // 循环依赖任务
    ALL("cdt");           // not supported

    private static final DAGJobType[] VALS = values();

    public final String value;

    private DAGJobType(String value) {
        this.value = value;
    }

    public static DAGJobType getInstance(String s) {
        for (DAGJobType type : VALS) {
            if (type.value.equals(s)) {
                return type;
            }
        }

        return null;
    }

    /**
     * return true if this type implies that type
     *
     * @param that
     */
    public boolean implies(DAGJobType that) {
        if (that != null) {
            return (ordinal() & that.ordinal()) == that.ordinal();
        }
        return false;
    }

    /** AND operation. */
    public DAGJobType and(DAGJobType that) {
        return VALS[ordinal() & that.ordinal()];
    }

    /** OR operation. */
    public DAGJobType or(DAGJobType that) {
        return VALS[ordinal() | that.ordinal()];
    }

    /** NOT operation. */
    public DAGJobType not() {
        return VALS[7 - ordinal()];
    }

    /**
     * remove that type from this
     */
    public DAGJobType remove(DAGJobType that) {
        return and(that.not());
    }

    public static DAGJobType getDAGJobType(int timeFlag,  int dependFlag, int cycleFlag) {
        DAGJobType[] values = DAGJobType.values();
        return values[(cycleFlag << 2) + (dependFlag << 1) + timeFlag];
    }
}
