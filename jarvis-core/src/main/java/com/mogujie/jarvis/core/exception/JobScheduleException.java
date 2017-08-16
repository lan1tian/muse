/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月1日 下午2:04:58
 */

package com.mogujie.jarvis.core.exception;

/**
 * @author guangming
 *
 */
public class JobScheduleException extends JarvisException {

    private static final long serialVersionUID = 1L;

    public JobScheduleException() {
        super();
    }

    public JobScheduleException(final String message) {
        super(message);
    }

    public JobScheduleException(Throwable cause) {
        super(cause);
    }

    public JobScheduleException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
