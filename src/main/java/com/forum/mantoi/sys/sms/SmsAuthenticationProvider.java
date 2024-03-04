package com.forum.mantoi.sys.sms;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 这个短信的认证提供者需要使用SmsUserDetailsService
 *
 * @author DELL
 */

@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

    @Qualifier("smsUserDetailsService")
    private final UserDetailsService userDetailsService;

    private final StringRedisTemplate redisTemplate;

    public SmsAuthenticationProvider(@Qualifier("smsUserDetailsService") UserDetailsService userDetailsService, StringRedisTemplate stringRedisTemplate) {
        this.userDetailsService = userDetailsService;
        this.redisTemplate = stringRedisTemplate;
    }

    /**
     * 负责实际的SMS认证逻辑
     *
     * @param authentication authentication
     * @return Authentication
     * @throws AuthenticationException exception
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = ((SmsAuthenticationToken) authentication);
        Object principal = authentication.getPrincipal();
        String phone = "";
        if (principal instanceof String) {
            phone = ((String) principal);
        }
        String inputCode = (String) authenticationToken.getCredentials();
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (StringUtils.isBlank(redisCode)) {
            throw new BadCredentialsException("Sms Cache not found");
        }
        if (!inputCode.equals(redisCode)) {
            throw new BadCredentialsException("Sms code wrong");
        }
        redisTemplate.delete(phone);

        UserDetails userDetails = userDetailsService.loadUserByUsername(phone);
        if (Objects.isNull(userDetails)) {
            throw new InternalAuthenticationServiceException("User not found");
        }
        SmsAuthenticationToken authenticatedToken = new SmsAuthenticationToken(principal, inputCode, userDetails.getAuthorities());
        authenticatedToken.setDetails(authentication.getDetails());
        return authenticatedToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
