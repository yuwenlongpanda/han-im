package com.ywl.framework.mysql.shard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author liao
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface TableShardField {
}
