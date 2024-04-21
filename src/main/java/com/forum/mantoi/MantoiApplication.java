package com.forum.mantoi;

import com.forum.mantoi.sys.dao.repository.InvertIndexRepository;
import com.forum.mantoi.utils.TokenBucketLimiter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

/**
 * @author DELL
 */
@SpringBootApplication
@MapperScan(value = "com.forum.mantoi.sys.dao.mapper")
public class MantoiApplication {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    public static void main(String[] args) {
        SpringApplication.run(MantoiApplication.class, args);
    }


}
