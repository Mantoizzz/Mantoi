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
public class CaffeineConfiguration {

    @Bean(name = "caffeineCacheManager")
    public Cache<String, Object> localCacheManager() {
        int expireTime = 10;
        int initialCapacity = 50;
        int maximumSize = 500;
        return Caffeine.newBuilder()
                .expireAfterWrite(expireTime, TimeUnit.MINUTES)
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .recordStats()
                .build();

    }
}
