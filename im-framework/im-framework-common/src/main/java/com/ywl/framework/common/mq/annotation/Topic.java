package com.ywl.framework.common.mq.annotation;

import java.lang.annotation.*;

/**
 * @author liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Topic {
    String value();
}
