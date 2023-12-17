package com.forum.mantoi.sys.filter;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.common.Constants;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.JwtUser;
import com.forum.mantoi.sys.repository.UserRepository;
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
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtilities jwtUtilities;

    private final UserRepository userRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(Constants.JWT_HEADER);

        if (token != null && jwtUtilities.validateToken(token)) {
            String email = jwtUtilities.extractEmail(token);

            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                throw new UserException(CommonResultStatus.FAIL, "Email does not refer to anyone");
            }
            UserDetails userDetails = new JwtUser(user.get());
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
