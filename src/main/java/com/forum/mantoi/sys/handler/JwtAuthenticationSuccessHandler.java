package com.forum.mantoi.sys.handler;

import com.forum.mantoi.common.constant.Constants;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.SysUser;
import com.forum.mantoi.utils.JwtUtils;
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

import java.util.Objects;

/**
 * @author Mantoi
 */
@AllArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserMapper userMapper;

    private final StringRedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws UserException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("authenticationName:{}", authentication.getName());
        User user = userMapper.findByUsername(authentication.getName());
        if (Objects.isNull(user)) {
            throw new UserException(CommonResultStatus.RECORD_NOT_EXIST, CommonResultStatus.RECORD_NOT_EXIST.getMsg());
        }
        SysUser sysUser = new SysUser(user);
        String token = JwtUtils.generateToken(user.getEmail(), sysUser.getAuthorities());
        //删除Redis中的黑名单key
        String key = RedisKeys.getBlackListTokenKey(user.getUsername());
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.delete(key);
        }
        log.info("token:{}", token);
        response.setHeader(Constants.JWT_HEADER, token);
    }

}
