/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月15日 下午3:24:50
 */

package com.mogujie.jarvis.rest.sentinel;

import java.util.Map;

public class BaseRet extends ResponseParams {

    @Override
    public String toString() {
        return "BaseRet [respCode=" + respCode + ", respMsg=" + respMsg
                + ", toString()=" + super.toString() + "]";
    }

    private static final long serialVersionUID = -7564916891623575871L;

    private Integer respCode;
    private String respMsg;

    public BaseRet(ResponseCodeEnum responseCode) {
        this.respCode = responseCode.getValue();
        this.respMsg = responseCode.getDesc();
    }

    public BaseRet(ResponseCodeEnum responseCode, String message) {
        this.respCode = responseCode.getValue();

        if (message != null && message.length() > 0) {
            this.respMsg = message;
        } else {
            this.respMsg = responseCode.getDesc();
        }

    }

    public BaseRet(ResponseCodeEnum responseCode, String message,
            Map<String, Object> params) {
        this.respCode = responseCode.getValue();

        if (message != null && message.length() > 0) {
            this.respMsg = message;
        } else {
            this.respMsg = responseCode.getDesc();
        }

        this.params = params;

    }

    public BaseRet(Integer responseCode, String responseMsg) {
        this.respCode = responseCode;
        this.respMsg = responseMsg;
    }

    /**
     * @return the respCode
     */
    public Integer getRespCode() {
        return respCode;
    }

    /**
     * @param respCode
     *            the respCode to set
     */
    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    /**
     * @return the respMsg
     */
    public String getRespMsg() {
        return respMsg;
    }

    /**
     * @param respMsg
     *            the respMsg to set
     */
    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return this.respCode.equals(ResponseCodeEnum.SUCCESS.getValue());
    }

    /**
     * 是否请求参数错误
     *
     * @return
     */
    public boolean isWrongParams() {
        return this.respCode.equals(ResponseCodeEnum.WRONG_PARAMS.getValue());
    }

    /**
     * 请求是否被拒绝
     *
     * @return
     */
    public boolean isRejected() {
        return this.respCode.equals(ResponseCodeEnum.REJECT.getValue());
    }
}
