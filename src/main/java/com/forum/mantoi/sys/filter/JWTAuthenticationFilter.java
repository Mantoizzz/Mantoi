package com.forum.mantoi.sys.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forum.mantoi.sys.model.JwtUser;
import com.forum.mantoi.sys.model.LoginUser;
import com.forum.mantoi.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;

@Component
@AllArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ThreadLocal<Integer> rememberMe = new ThreadLocal<>();

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
            rememberMe.set(loginUser.getRememberMe() == null ? 0 : loginUser.getRememberMe());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayDeque<>()));
        } catch (IOException e) {
            log.info("JwtAuthenticationFilter:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        String role = "";
        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
        }
        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), role, rememberMe.get() == 1);

        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().write("Authentication failed:" + failed.getMessage());
    }
}
