/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月23日 下午6:59:34
 */

package com.mogujie.jarvis.rest.jarvis;

/**
 * 把自己持久成String对象以及从String对象重现建立起来
 *
 * @author yinxiu
 * @version $Id: SelfDependence.java,v 0.1 2014年5月20日 下午2:32:30 yinxiu Exp $
 */
public interface SelfDependency {
    public String lieDown();

    public SelfDependency riseUp(String value);
}
