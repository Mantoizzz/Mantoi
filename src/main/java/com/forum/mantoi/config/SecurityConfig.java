package com.forum.mantoi.config;

import com.forum.mantoi.sys.filter.JwtAuthenticationFilter;
import com.forum.mantoi.sys.handler.JwtTokenAuthenticationSuccessHandler;
import com.forum.mantoi.sys.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtTokenAuthenticationSuccessHandler jwtTokenAuthenticationSuccessHandler;

    private final String EMAIL_PARAMETER = "email";

    private final String PASSWORD_PARAMETER = "password";

    private final String LOGIN_PAGE = "/auth/login";

    private final String LOGIN_PROCESSING_URL = "/auth/login";

    private final String[] PUBLIC_URL = {"/auth/**", "/homePage"};

    private final String[] ANONYMOUS_URL = {"/homePage"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(config -> config
                .usernameParameter(EMAIL_PARAMETER)
                .passwordParameter(PASSWORD_PARAMETER)
                .loginPage(LOGIN_PAGE)
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .successHandler(jwtTokenAuthenticationSuccessHandler)
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
