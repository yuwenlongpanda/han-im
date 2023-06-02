package com.ywl.framework.mq;

import org.springframework.beans.factory.FactoryBean;

import static com.ywl.framework.mq.MessageQueueInvocationHandler.generateProxyClass;

public class MessageQueueFactoryBean<T> implements FactoryBean<T> {

    public final Class<T> type;

    public MessageQueueFactoryBean(Class<T> type) {
        this.type = type;
    }


    @Override
    public T getObject() throws Exception {
        return (T) generateProxyClass(type);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }
}
