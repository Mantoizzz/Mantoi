package com.forum.mantoi.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine Configuration
 *
 * @author DELL
 */
@Configuration
public class CaffeineConfig {

    @Bean(name = "caffeineCacheManager")
    public Cache<String, Object> localCacheManager() {
        int expireTime = 2;
        int initialCapacity = 50;
        int maximumSize = 200;
        return Caffeine.newBuilder()
                .expireAfterWrite(expireTime, TimeUnit.DAYS)
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .recordStats()
                .build();
    }
}
