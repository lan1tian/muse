/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月2日 上午10:30:39
 */

package com.mogujie.jarvis.core.observer;


/**
 * @author guangming
 *
 */
public interface Observable {
    /**
     * register a observer to observable
     */
    public void register(Observer o);

    /**
     * unregister a observer from observable
     */
    public void unregister(Observer o);

    /**
     * notify event to all listeners
     */
    public void notify(Event event) throws Exception;
}
