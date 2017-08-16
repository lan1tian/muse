package com.mogujie.jarvis.web.utils;

/**
 * Created by hejian on 16/1/14.
 */
public enum MessageStatus {
    SUCCESS(1000, "成功"),
    FAILED(1001, "异常");

    private Integer value;
    private String text;

    MessageStatus(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
