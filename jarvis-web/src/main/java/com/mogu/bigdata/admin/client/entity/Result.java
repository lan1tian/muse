package com.mogu.bigdata.admin.client.entity;

import com.mogujie.jarvis.web.mock.MockUuapService;

/**
 * Created by lixun on 2017/7/25.
 */
public class Result<T> {

    private String code;
    private String msg ;
    private T result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
