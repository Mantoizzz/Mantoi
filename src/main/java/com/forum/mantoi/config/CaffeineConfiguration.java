package com.forum.mantoi.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine Configuration
 */
@Configuration
public class CaffeineConfiguration {

    private final int EXPIRE_TIME = 10;

    private final int INITIAL_CAPACITY = 50;

    private final int MAXIMUM_SIZE = 500;

    @Bean(name = "caffeineCacheManager")
    public Cache<String, Object> localCacheManager() {
        return Caffeine.newBuilder()
                .expireAfterWrite(EXPIRE_TIME, TimeUnit.MINUTES)
                .initialCapacity(INITIAL_CAPACITY)
                .maximumSize(MAXIMUM_SIZE)
                .recordStats()
                .build();

    }
}
