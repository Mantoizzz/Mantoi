package com.forum.mantoi.config;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.constant.Constants;
import com.forum.mantoi.sys.filter.JwtAuthenticationFilter;
import com.forum.mantoi.sys.handler.JwtAuthenticationSuccessHandler;
import com.forum.mantoi.sys.handler.MyAuthenticationFailureHandler;
import com.forum.mantoi.sys.services.impl.DaoUserDetailsService;
import com.forum.mantoi.sys.services.impl.SmsUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author DELL
 */
@Configuration
@AllArgsConstructor
public class SecurityConfig implements ApiRouteConstants {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(config ->
                config.requestMatchers(API_AUTH_PREFIX + API_ALL).permitAll()
        );

//        http.formLogin(config -> {
//                    try {
//                        config
//                                .usernameParameter(Constants.EMAIL)
//                                .passwordParameter(Constants.PASSWORD)
//                                .loginPage(ApiRouteConstants.API_LOGIN_URL)
//                                .loginProcessingUrl(ApiRouteConstants.API_LOGIN_URL)
//                                .successHandler(jwtAuthenticationSuccessHandler);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//        );

//        http.apply(SmsLoginConfigurer.smsLogin())
//                .successHandler(jwtAuthenticationSuccessHandler).failureHandler(failureHandler)
//                .phoneParameter("phone")
//                .smsCodeParameter("smsCode")
//                .configure(http.authenticationManager(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))));

        http.logout(config -> config
                .logoutUrl(ApiRouteConstants.API_LOGOUT_URL)
                .clearAuthentication(true)
        );


        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
