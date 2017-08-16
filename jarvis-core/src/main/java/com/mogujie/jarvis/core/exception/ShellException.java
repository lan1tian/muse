/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午2:52:35
 */

package com.mogujie.jarvis.core.exception;

/**
 * @author muming
 *
 */
public class ShellException extends JarvisException {

    private static final long serialVersionUID = 1L;

    public ShellException() {
        super();
    }

    public ShellException(final String message) {
        super(message);
    }

    public ShellException(Throwable cause) {
        super(cause);
    }

    public ShellException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
