package com.ywl.framework.web.aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.Md5Utils;
import com.ywl.framework.common.context.UserContext;
import com.ywl.framework.redis.RedisClient;
import com.ywl.framework.web.result.BaseResultCodeEnum;
import com.ywl.framework.web.result.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RepeatRequestAspect {

    private static final String REPEAT_REQUEST_REDIS_KEY = "repeat_request_redis_key:";

    @Resource
    private RedisClient redisClient;

    @Around("@annotation(com.ywl.framework.web.aop.RepeatRequestIntercept)")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        Long userId = UserContext.getUserId();
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String requestInfo = joinPoint.getTarget() + method.getName() + userId + userId + JSON.toJSONString(args);

        String md5 = Md5Utils.getMD5(requestInfo.getBytes());
        String lockKey = REPEAT_REQUEST_REDIS_KEY.concat(md5);
        Boolean lockFlag = false;
        try {
            lockFlag = redisClient.setNx(lockKey, md5);
            if (Boolean.TRUE.equals(lockFlag)) {
                return joinPoint.proceed();
            } else {
                throw new BizException(BaseResultCodeEnum.REPETITIVE_OPERATION);
            }
        } finally {
            if (Boolean.TRUE.equals(lockFlag)) {
                redisClient.delete(lockKey);
            }
        }
    }
}
