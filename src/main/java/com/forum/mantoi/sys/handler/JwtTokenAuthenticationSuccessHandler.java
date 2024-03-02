package com.forum.mantoi.sys.handler;

import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.constant.Constants;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.JwtUser;
import com.forum.mantoi.sys.dao.repository.UserRepository;
import com.forum.mantoi.utils.JwtUtilities;
import com.forum.mantoi.utils.RedisKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author Mantoi
 */
@AllArgsConstructor
@Component
@Slf4j
public class JwtTokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final JwtUtilities jwtUtilities;

    private final StringRedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws UserException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("authenticationName:{}", authentication.getName());
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserException(CommonResultStatus.FAIL, "User not found"));
        JwtUser jwtUser = new JwtUser(user);
        String token = jwtUtilities.generateToken(user.getEmail(), jwtUser.getAuthorities());
        //删除Redis中的黑名单key
        String key = RedisKeys.getBlackListTokenKey(user.getUsername());
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.delete(key);
        }
        log.info("token:{}", token);
        response.setHeader(Constants.JWT_HEADER, token);
    }

}
