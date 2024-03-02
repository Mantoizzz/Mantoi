package com.forum.mantoi.config;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.constant.Constants;
import com.forum.mantoi.sys.filter.JwtAuthenticationFilter;
import com.forum.mantoi.sys.handler.JwtTokenAuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtTokenAuthenticationSuccessHandler jwtTokenAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(config -> config
                .usernameParameter(Constants.EMAIL)
                .passwordParameter(Constants.PASSWORD)
                .loginPage(ApiRouteConstants.API_LOGIN_URL)
                .loginProcessingUrl(ApiRouteConstants.API_LOGIN_URL)
                .successHandler(jwtTokenAuthenticationSuccessHandler)
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
