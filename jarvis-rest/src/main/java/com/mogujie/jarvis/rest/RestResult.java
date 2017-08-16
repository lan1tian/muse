package com.mogujie.jarvis.rest;

import com.mogujie.jarvis.rest.vo.AbstractVo;

/**
 * REST结果
 * 
 * @author 牧名
 */
public class RestResult{

    /** 错误码 */
    private int code;
    /** 错误信息 */
    private String msg;
    /** 数据 */
    private AbstractVo data;
    /**
     * 构造
     */
    public RestResult() {
    }
    /**
     * 构造
     */
    public RestResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public AbstractVo getData() {
        return data;
    }
    public void setData(AbstractVo data) {
        this.data = data;
    }

}
