package com.forum.mantoi.config;

import com.forum.mantoi.common.aop.RateLimiterAOP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

/**
 * @author DELL
 */
@Slf4j
@Controller
public class RateLimiterAOPConfig {

    @Bean
    public RateLimiterAOP rateLimiterAop() {
        return new RateLimiterAOP();
    }

}
