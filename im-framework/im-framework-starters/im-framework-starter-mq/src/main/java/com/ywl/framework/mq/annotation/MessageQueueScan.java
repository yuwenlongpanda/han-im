package com.ywl.framework.mq.annotation;


import com.ywl.framework.mq.MessageQueueRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MessageQueueRegistrar.class)
public @interface MessageQueueScan {

    String [] value();
}
