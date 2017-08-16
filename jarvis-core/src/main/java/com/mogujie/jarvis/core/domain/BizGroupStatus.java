package com.mogujie.jarvis.core.domain;

/**
 * Created by hejian on 16/1/15.
 */
public enum BizGroupStatus {
    ENABLE(1, "启用"),
    DISABLED(2, "禁用");

    private int value;
    private String description;

    BizGroupStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }


    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static Boolean isValid(int value) {
        BizGroupStatus[] values = BizGroupStatus.values();
        for (BizGroupStatus s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public static BizGroupStatus parseValue(int value) throws IllegalArgumentException {
        BizGroupStatus[] statusList = BizGroupStatus.values();
        for (BizGroupStatus s : statusList) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("WorkerGroupStatus value is invalid. value:" + value);
    }
}
