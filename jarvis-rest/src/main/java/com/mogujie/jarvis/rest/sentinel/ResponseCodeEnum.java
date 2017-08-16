/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月15日 下午3:25:42
 */

package com.mogujie.jarvis.rest.sentinel;

public enum ResponseCodeEnum {

    SUCCESS(0, "成功"), FAILED(1, "失败"), REJECT(2, "拒绝"), VALDATION_FAILED(-1, "请求验证失败"), WRONG_PARAMS(
            -2, "请求参数错误");

    ResponseCodeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
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
        return this.value == value;
    }
}
