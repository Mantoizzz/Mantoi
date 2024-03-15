package com.forum.mantoi.sys.filter;

import com.forum.mantoi.sys.sms.SmsAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Objects;


/**
 * @author DELL
 */
@Component
@Getter
@Setter
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher SMS_LOGIN = new AntPathRequestMatcher("/auth/sms/captcha", HttpMethod.POST.name());

    private String phoneParam = "phone";

    private String smsCodeParam = "smsCode";

    private boolean postOnly = true;

    public SmsAuthenticationFilter() {
        super(SMS_LOGIN);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Http Method does not accept");
        }
        String phone = getPhone(request);
        phone = Objects.isNull(phone) ? "" : phone.trim();
        String smsCode = getSmsCode(request);
        smsCode = Objects.isNull(smsCode) ? "" : smsCode;

        SmsAuthenticationToken authenticationToken = new SmsAuthenticationToken(phone, smsCode);
        this.setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Nullable
    protected String getSmsCode(HttpServletRequest request) {
        return request.getParameter(this.smsCodeParam);
    }

    @Nullable
    protected String getPhone(HttpServletRequest request) {
        return request.getParameter(this.phoneParam);
    }

    protected void setDetails(HttpServletRequest request, SmsAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
