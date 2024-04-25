package com.forum.mantoi.sys.handler;

import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.exception.AuthException;
import com.forum.mantoi.utils.JwtUtils;
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

/**
 * @author DELL
 */
@Component
@Slf4j
@AllArgsConstructor
public class MyLogoutHandler implements LogoutHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = JwtUtils.getToken(request);
        String username = authentication.getName();
        //由于token不多，所以采用为每个黑名单键设置一个key的策略
        int expiration = 7;
        if (token != null) {
            redisTemplate.opsForValue().set(RedisKeys.getBlackListTokenKey(username), token, expiration, TimeUnit.DAYS);
        }
        SecurityContextHolder.clearContext();
        try {
            response.sendRedirect("/homePage");
        } catch (IOException e) {
            log.info("response发送重定向抛出IOException");
            throw new AuthException(CommonResultStatus.SERVER_ERROR, CommonResultStatus.SERVER_ERROR.getMsg());
        }
    }
}
