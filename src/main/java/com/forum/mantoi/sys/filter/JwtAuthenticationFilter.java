package com.forum.mantoi.sys.filter;

import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.AuthException;
import com.forum.mantoi.sys.model.SysUser;
import com.forum.mantoi.utils.JwtUtils;
import com.forum.mantoi.utils.RedisKeys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 拿到Header中的JWT,如果JWT未过期，走正常鉴权登录
 * 如果过期了，判断过期了几天，如果是3天以内，生成新的JWT然后走正常鉴权登录
 * 否则直接表明没有登录
 *
 * @author DELL
 */
@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserMapper userMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwt = JwtUtils.getToken(request);
        if (Objects.isNull(jwt) || !isValid(jwt)) {
            filterChain.doFilter(request, response);
        }
        LocalDate expiredTime = JwtUtils.extractExpiration(jwt).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        long days = ChronoUnit.DAYS.between(expiredTime, now);
        String email = JwtUtils.extractEmail(jwt);
        //表明还没过期
        if (expiredTime.isAfter(now)) {
            processAuthentication(email, filterChain, request, response);
        }
        if (expiredTime.isBefore(now) && days <= 3) {
            User user = userMapper.findByEmail(email);
            UserDetails userDetails = new SysUser(user);
            String newJwt = JwtUtils.generateToken(user.getEmail(), userDetails.getAuthorities());
            response.addHeader("Authorization", newJwt);
            processAuthentication(email, filterChain, request, response);
        }
        filterChain.doFilter(request, response);
    }


    /**
     * 正常鉴权流程
     *
     * @param email       email
     * @param filterChain filterChain
     * @param request     request
     * @param response    response
     * @throws ServletException Exception
     * @throws IOException      Exception
     */
    private void processAuthentication(String email, @NonNull FilterChain filterChain, @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws ServletException, IOException {
        User user = userMapper.findByEmail(email);
        if (Objects.isNull(user)) {
            throw new AuthException(CommonResultStatus.UNAUTHORIZED, "User not found");
        }
        UserDetails userDetails = new SysUser(user);
        String blacklist = RedisKeys.getBlackListTokenKey(userDetails.getUsername());
        if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklist))) {
            filterChain.doFilter(request, response);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    /**
     * 判断JWT是否在黑名单
     *
     * @param jwt JWT
     * @return bool
     */
    private boolean isValid(String jwt) {
        String tokenId = JwtUtils.extractAllClaims(jwt).getId();
        //说明在黑名单里面
        return !Boolean.TRUE.equals(redisTemplate.hasKey(tokenId));
    }
}
