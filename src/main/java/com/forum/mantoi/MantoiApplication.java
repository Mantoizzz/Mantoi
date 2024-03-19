package com.forum.mantoi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

/**
 * @author DELL
 */
@SpringBootApplication
@MapperScan(value = "com.forum.mantoi.sys.dao.mapper")
@EnableJpaRepositories(basePackages = "com.forum.mantoi.sys.dao.repository")
public class MantoiApplication {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    public static void main(String[] args) {
        SpringApplication.run(MantoiApplication.class, args);
    }


}
