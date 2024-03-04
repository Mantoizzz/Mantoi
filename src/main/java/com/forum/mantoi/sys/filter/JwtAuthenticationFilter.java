package com.forum.mantoi.sys.filter;

import com.forum.mantoi.common.constant.Constants;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.SysUser;
import com.forum.mantoi.utils.JwtUtilities;
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
import java.util.Objects;

/**
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
        String token = request.getHeader(Constants.JWT_HEADER);

        if (token != null && JwtUtilities.validateToken(token)) {
            String email = JwtUtilities.extractEmail(token);

            User user = userMapper.findByEmail(email);
            if (Objects.isNull(user)) {
                throw new UserException(CommonResultStatus.FAIL, "Email does not refer to anyone");
            }
            UserDetails userDetails = new SysUser(user);
            String blacklistTokenKey = RedisKeys.getBlackListTokenKey(userDetails.getUsername());
            if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklistTokenKey))) {
                filterChain.doFilter(request, response);
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("User authenticated with email {}", email);
        }
        filterChain.doFilter(request, response);
    }
}
