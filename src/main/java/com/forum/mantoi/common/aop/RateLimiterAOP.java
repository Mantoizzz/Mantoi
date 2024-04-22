package com.forum.mantoi.common.aop;

import com.forum.mantoi.common.annotation.AccessInterceptor;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.exception.RateLimitException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.util.concurrent.RateLimiter;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author DELL
 */
@Slf4j
@Aspect
public class RateLimiterAOP {

    private final Cache<String, RateLimiter> loginRecord = Caffeine.newBuilder().
            expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    private final Cache<String, Long> blackList = Caffeine.newBuilder().
            expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @Pointcut("@annotation(com.forum.mantoi.common.annotation.AccessInterceptor)")
    public void aopPoint() {

    }

    @Around("aopPoint()&&@annotation(accessInterceptor)")
    public Object doRouter(ProceedingJoinPoint jp, AccessInterceptor accessInterceptor) throws Throwable {
        String key = accessInterceptor.key();
        if (StringUtils.isBlank(key)) {
            throw new RateLimitException(CommonResultStatus.PARAM_ERROR, "annotation key is blank");
        }
        String keyAttr = getAttrValue(key, jp.getArgs());
        //如果字段不为"all"，并且注解中配置中并非不加入黑名单，且拦截次数超过了注解中的最大拦截次数，此时调用fallbackMethod拦截
        if (!"all".equals(keyAttr) && accessInterceptor.blackListCount() != 0
                && null != blackList.getIfPresent(keyAttr)
                && blackList.getIfPresent(keyAttr) > accessInterceptor.blackListCount()) {
            log.info("该字段已经被黑名单自动拦截:{}", keyAttr);
            return fallbackMethodResult(jp, accessInterceptor.fallbackMethod());
        }

        RateLimiter rateLimiter = loginRecord.getIfPresent(keyAttr);
        if (Objects.isNull(rateLimiter)) {
            rateLimiter = RateLimiter.create(accessInterceptor.permitsPerSecond());
            loginRecord.put(keyAttr, rateLimiter);
        }
        //触发限流
        if (!rateLimiter.tryAcquire()) {
            if (accessInterceptor.blackListCount() != 0) {
                if (Objects.isNull(blackList.getIfPresent(keyAttr))) {
                    blackList.put(keyAttr, 1L);
                } else {
                    blackList.put(keyAttr, blackList.getIfPresent(keyAttr) + 1);
                }
            }
            log.info("限流拦截:{}", keyAttr);
            return fallbackMethodResult(jp, accessInterceptor.fallbackMethod());
        }

        return jp.proceed();
    }

    private Object fallbackMethodResult(JoinPoint jp, String fallbackMethod) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = jp.getTarget().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        return method.invoke(jp.getThis(), jp.getArgs());
    }

    public String getAttrValue(String attr, Object[] args) {
        if (args[0] instanceof String) {
            return args[0].toString();
        }
        String fieldValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(fieldValue)) {
                    break;
                }
                fieldValue = String.valueOf(this.getValueByName(arg, attr));
            } catch (Exception e) {
                log.error("getAttrValue error attr{}", attr, e);
            }
        }
        return fieldValue;
    }

    /**
     * 获取对象的属性名
     *
     * @param item 对象
     * @param name 字段名字
     * @return 属性值
     */
    private Object getValueByName(Object item, String name) {
        try {
            Field field = getFieldByName(item, name);
            if (Objects.isNull(field)) {
                return null;
            }
            field.setAccessible(true);
            Object value = field.get(item);
            field.setAccessible(false);
            return value;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 通过类名和字段名拿字段值
     *
     * @param item      类名
     * @param fieldName 字段名
     * @return Field
     * @see Field
     */
    private Field getFieldByName(Object item, String fieldName) {
        try {
            return processField(item, fieldName);
        } catch (NoSuchFieldException exception) {
            return null;
        }
    }

    private Field processField(Object item, String fieldName) throws NoSuchFieldException {
        Field field;
        try {
            field = item.getClass().getField(fieldName);
        } catch (NoSuchFieldException exception) {
            field = item.getClass().getSuperclass().getField(fieldName);
        }
        return field;
    }
}
