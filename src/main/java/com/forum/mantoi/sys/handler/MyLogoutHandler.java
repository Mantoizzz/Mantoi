package com.forum.mantoi.sys.handler;

import com.forum.mantoi.common.Constants;
import com.forum.mantoi.utils.JwtUtilities;
import com.forum.mantoi.utils.RedisKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@AllArgsConstructor
public class MyLogoutHandler implements LogoutHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    private final JwtUtilities jwtUtilities;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtUtilities.getToken(request);
        String username = authentication.getName();
        //由于token不多，所以采用为每个黑名单键设置一个key的策略
        int BLACK_TOKEN_EXPIRATION = 7;
        redisTemplate.opsForValue().set(RedisKeys.getBlackListTokenKey(username), token, BLACK_TOKEN_EXPIRATION, TimeUnit.DAYS);
        SecurityContextHolder.clearContext();
        try {
            response.sendRedirect("/homePage");
        } catch (IOException e) {
            log.info("response发送重定向抛出IOException");
            throw new RuntimeException(e);
        }
    }
}
