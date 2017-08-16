package com.mogujie.bigdata.base.domain;

import com.mogujie.jarvis.web.entity.utils.MessageCode;

/**
 * Created by lixun on 2017/7/25.
 */
public class JSONReturn {

    private int respCode;
    private String respMsg;
    private String retCont;

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getRetCont() {
        return retCont;
    }

    public void setRetCont(String retCont) {
        this.retCont = retCont;
    }
}
