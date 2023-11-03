package com.forum.mantoi.sys.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.sys.exception.TokenExpiredException;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtToken = request.getHeader(JwtTokenUtils.TOKEN_HEADER);

        if (jwtToken == null || !jwtToken.startsWith(JwtTokenUtils.TOKEN_HEADER)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(jwtToken));
        } catch (TokenExpiredException | UserException e) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            String log = "reason:" + e.getMessage();
            response.getWriter().write(new ObjectMapper().writeValueAsString(log));
            response.getWriter().flush();
            return;
        }
        super.doFilterInternal(request, response, chain);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) throws TokenExpiredException, UserException {
        String token = tokenHeader.replace(JwtTokenUtils.TOKEN_HEADER, "");
        boolean expired = JwtTokenUtils.isExpiration(token);
        if (expired) {
            throw new TokenExpiredException(CommonResultStatus.UNAUTHORIZED, "Token is expired");
        } else {
            String username = JwtTokenUtils.getUsername(token);
            String role = JwtTokenUtils.getUserRole(token);
            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(new SimpleGrantedAuthority(role)));
            } else {
                throw new UserException(CommonResultStatus.UNAUTHORIZED, "Can't extract user from token");
            }
        }
    }
}
