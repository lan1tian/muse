/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月15日 下午3:49:52
 */

package com.mogujie.jarvis.rest.sentinel;

import java.util.HashMap;
import java.util.Map;

public enum JobStatusEnum {
    SUCCESS(0, "成功"), WAIT(1, "等待"), ACCEPT(2, "接收"), RUNNING(3, "执行中"), ERROR(4, "错误"), FAIL(5, "失败"), RECOVER(6, "恢复"), EXCEPTION(7, "异常"), KILLED(
            8, "KILLED");
    JobStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Object, JobStatusEnum> pool = new HashMap<Object, JobStatusEnum>();
    static {
        for (JobStatusEnum each : JobStatusEnum.values()) {
            JobStatusEnum defined = pool.get(each.value);
            // 有相同权限代码定义抛出例外
            if (null != defined) {
                throw new java.lang.IllegalArgumentException(defined.toString() + " defined as same code with " + each.toString());
            }
            pool.put(each.value, each);
        }
    }

    private Integer value;
    private String desc;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public final boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static String getDesc(int code) {
        JobStatusEnum s = pool.get(code);
        if (s == null) {
            return null;
        }
        return s.getDesc();
    }

    public static JobStatusEnum get(Integer code) {
        return pool.get(code);
    }

    public boolean isSuccess() {
        return this.value.intValue() == 0;
    }

    public boolean isFinishStatus() {
        int status = this.value.intValue();
        if (status == 0 || status == 4 || status == 5 || status == 7 || status == 8) {
            return true;
        } else {
            return false;
        }
    }
}
