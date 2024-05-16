package com.forum.mantoi.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Cache;

/**
 * @author DELL
 */
public final class CaffeineUtils {

    private static final Cache<String, Object> CAFFEINE_CACHE = SpringUtil.getBean("caffeineCacheManager");

    private CaffeineUtils() {
    }

    public static Object get(String key) {
        return CAFFEINE_CACHE.getIfPresent(key);
    }

    public static void put(String key, Object value) {
        CAFFEINE_CACHE.put(key, value);
    }


}
