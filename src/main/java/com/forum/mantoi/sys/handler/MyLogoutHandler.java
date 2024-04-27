package com.forum.mantoi.sys.handler;

import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.exception.AuthException;
import com.forum.mantoi.utils.JwtUtils;
import com.forum.mantoi.utils.RedisKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 先拿到request的token
 * 然后拿到token还有多久过期
 * 在redis中存储<Token Id,days + 1>键值对
 * 到时候当黑名单结束后这个JWT会自动过期
 *
 * @author DELL
 */
@Component
@Slf4j
@AllArgsConstructor
public class MyLogoutHandler implements LogoutHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void logout(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Authentication authentication) {
        String token = JwtUtils.getToken(request);
        String tokenId = JwtUtils.extractAllClaims(token).getId();
        LocalDate expiredDate = JwtUtils.extractExpiration(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        Duration duration = Duration.between(now, expiredDate);
        long days = duration.toDays();
        redisTemplate.opsForValue().set(tokenId, 1, days + 1, TimeUnit.DAYS);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
