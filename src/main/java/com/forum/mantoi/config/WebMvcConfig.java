package com.forum.mantoi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.forum.mantoi.common.constant.ApiRouteConstants.*;


/**
 * @author DELL
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(API_LOGIN_URL).setViewName("login");
        registry.addViewController(API_REGISTER_URL).setViewName("register");
        registry.addViewController(API_POST_PREFIX).setViewName("post");
    }

}
