package com.ywl.framework.redis.annotation;

import java.lang.annotation.*;

/**
 * 缓存过期注解（配合@Cachebale使用）
 * <p>
 * 1.支持防缓存穿透
 * 2.支持方法嵌套（内部方法如果不指定过期时间，则继承外部方法的过期时间）
 * 如果不使用该注解指定@Cacheable的过期时间，将会采取RedisCacheAutoConfiguration中配置的全局缓存失效时间
 *
 * @author luozhan
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheExpire {

    /**
     * 失效时间（秒）
     */
    int value();

    /**
     * 上下浮动范围（秒）
     * 根据浮动范围生成随机秒数加在缓存失效时间上，防止缓存穿透
     * 默认10毫秒
     */
    double floatRange() default 0.01;

}
