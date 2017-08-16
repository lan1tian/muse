package com.mogujie.jarvis.web.entity.utils;

/**
 * Created by hejian on 16/3/11.
 */
public enum MessageCode {
    normal(1000,"成功"),
    exception(5000,"异常");


    private int code;
    private String text;

    MessageCode(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
