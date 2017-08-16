/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午2:52:35
 */

package com.mogujie.jarvis.core.exception;

public class AcceptanceException extends Exception {

    private static final long serialVersionUID = 1L;

    public AcceptanceException() {
        super();
    }

    public AcceptanceException(final String message) {
        super(message);
    }

    public AcceptanceException(Throwable cause) {
        super(cause);
    }

    public AcceptanceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
