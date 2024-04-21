package com.forum.mantoi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author DELL
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenApi() {
        return new OpenAPI().info(info());
    }

    private Info info() {
        return new Info()
                .title("Forum Mantoi")
                .description("基于SpringBoot 3.x的仿照牛客网的就业论坛")
                .contact(new Contact()
                        .email("641538994@qq.com")
                        .name("Kanon")
                )
                .version("1.0");
    }

}
