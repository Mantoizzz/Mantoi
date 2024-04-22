package com.forum.mantoi.common.annotation;


import java.lang.annotation.*;

/**
 * @author DELL
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AccessInterceptor {

    //用哪个字段作为拦截标识
    String key() default "all";

    //限流速率
    double permitsPerSecond() default 1.0;

    //黑名单拦截(多次拦截后加入黑名单)，默认不加入黑名单
    int blackListCount() default 0;

    //拦截后的执行方法
    String fallbackMethod();

}
