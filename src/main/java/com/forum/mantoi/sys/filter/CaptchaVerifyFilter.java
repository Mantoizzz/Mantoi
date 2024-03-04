package com.forum.mantoi.sys.filter;

import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.exception.CaptchaException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


/**
 * @author DELL
 */
@AllArgsConstructor
@Component
public class CaptchaVerifyFilter extends GenericFilterBean {

    private static final AntPathRequestMatcher LOGIN_PATH_MATCHER = new AntPathRequestMatcher("/auth/login", HttpMethod.POST.name());

    private static final String CAPTCHA_KEY = "captcha";

    private static final String CAPTCHA_STATIC_KEY = "captchaID";

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (LOGIN_PATH_MATCHER.matches((HttpServletRequest) servletRequest)) {
            try {
                String captcha = servletRequest.getParameter(CAPTCHA_KEY);
                String captchaId = servletRequest.getParameter(CAPTCHA_STATIC_KEY);
                if (StringUtils.isAnyBlank(captcha, captchaId)) {
                    throw new CaptchaException(CommonResultStatus.NULL.getMsg());
                }
                String cache = stringRedisTemplate.opsForValue().get(captchaId);
                if (StringUtils.isBlank(cache)) {
                    throw new CaptchaException(CommonResultStatus.FAIL, "Captcha expired");
                }
                if (!cache.equals(captcha)) {
                    throw new CaptchaException("Wrong Captcha");
                }
                stringRedisTemplate.delete(captchaId);
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (CaptchaException e) {
                authenticationFailureHandler.onAuthenticationFailure((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, e);
            }
        }
    }


}
