/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年10月16日 下午2:05:30
 */

package com.mogujie.jarvis.server.domain;

import com.google.protobuf.GeneratedMessage;
import com.mogujie.jarvis.core.domain.MessageType;

public class ActorEntry {

    private Class<? extends GeneratedMessage> requestClass;
    private Class<? extends GeneratedMessage> responseClass;
    private MessageType messageType;

    public ActorEntry(Class<? extends GeneratedMessage> requestClass, Class<? extends GeneratedMessage> responseClass, MessageType messageType) {
        this.requestClass = requestClass;
        this.responseClass = responseClass;
        this.messageType = messageType;
    }

    public Class<? extends GeneratedMessage> getRequestClass() {
        return requestClass;
    }

    public void setRequestClass(Class<? extends GeneratedMessage> requestClass) {
        this.requestClass = requestClass;
    }

    public Class<? extends GeneratedMessage> getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class<? extends GeneratedMessage> responseClass) {
        this.responseClass = responseClass;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

}
