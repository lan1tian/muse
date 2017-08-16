/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月22日 上午11:29:40
 */

package com.mogujie.jarvis.rest.jarvis;

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = 325114040047422777L;
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFaultMessage(String message) {
        this.success = false;
        this.message = message;
    }
}
