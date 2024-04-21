package com.forum.mantoi.config;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.sys.filter.JwtAuthenticationFilter;
import com.forum.mantoi.sys.handler.JwtAuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
