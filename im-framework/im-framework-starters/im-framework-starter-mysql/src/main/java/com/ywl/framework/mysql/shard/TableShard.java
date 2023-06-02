package com.ywl.framework.mysql.shard;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author liao
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface TableShard {
    Class<? extends TableShardStrategy> shardStrategy();

    int hashShard() default 0;
}
