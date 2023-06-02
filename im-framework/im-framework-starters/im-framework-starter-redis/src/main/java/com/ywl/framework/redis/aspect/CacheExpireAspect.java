package com.ywl.framework.redis.aspect;


import com.ywl.framework.redis.annotation.CacheExpire;
import com.ywl.framework.redis.annotation.CacheExpireHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * 缓存失效时间Aspect
 *
 * @author luozhan
 */
@Slf4j
@Aspect
@Component
public class CacheExpireAspect {

    /**
     * 设置方法对应的缓存过期时间
     */
    @Around("@annotation(com.ywl.framework.redis.annotation.CacheExpire)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CacheExpire cacheExpire = method.getAnnotation(CacheExpire.class);
        // 过期时间（毫秒）
        long cacheExpiredMilliseconds = 1000L * cacheExpire.value();
        // 上下浮动范围（毫秒）
        long floatRangeMilliseconds = (long) (1000 * RandomUtils.nextDouble(0, cacheExpire.floatRange()));
        //设置失效时间，加上随机浮动值防止缓存穿透
        CacheExpireHolder.set(cacheExpiredMilliseconds + floatRangeMilliseconds);
        log.info("缓存切面，设置过期时间{}ms", cacheExpiredMilliseconds + floatRangeMilliseconds);
        try {
            return joinPoint.proceed();
        } finally {
            //清除对象
            CacheExpireHolder.remove();
        }
    }
}
