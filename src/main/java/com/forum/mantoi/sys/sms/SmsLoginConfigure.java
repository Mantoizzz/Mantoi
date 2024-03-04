package com.forum.mantoi.sys.sms;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.sys.filter.SmsAuthenticationFilter;
import com.forum.mantoi.sys.handler.MyAuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @param <H>
 * @author DELL
 */
public final class SmsLoginConfigure<H extends HttpSecurityBuilder<H>>
        extends AbstractAuthenticationFilterConfigurer<H, SmsLoginConfigure<H>, SmsAuthenticationFilter> implements ApiRouteConstants {

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    private final SavedRequestAwareAuthenticationSuccessHandler defaultSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    private AuthenticationSuccessHandler successHandler;

    private LoginUrlAuthenticationEntryPoint urlAuthenticationEntryPoint;

    private AuthenticationFailureHandler failureHandler;

    public SmsLoginConfigure() {
        super(new SmsAuthenticationFilter(), API_AUTH_PREFIX + API_SMS);
        this.failureHandler = new MyAuthenticationFailureHandler();
        this.successHandler = defaultSuccessHandler;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    //TODO
    @Override
    public void configure(H http) throws Exception {

    }

    public SmsLoginConfigure<H> loginPage(String loginPage) {
        return super.loginPage(loginPage);
    }

    public SmsLoginConfigure<H> phoneParam(String phoneParam) {
        this.getAuthenticationFilter().setPhoneParam(phoneParam);
        return this;
    }

    public SmsLoginConfigure<H> smsCodeParam(String smsCodeParam) {
        this.getAuthenticationFilter().setSmsCodeParam(smsCodeParam);
        return this;
    }

    public SmsLoginConfigure<H> failureForwardUrl(String url) {
        this.failureHandler(new ForwardAuthenticationFailureHandler(url));
        return this;
    }

    public SmsLoginConfigure<H> successForwardUrl(String url) {
        this.successHandler(new ForwardAuthenticationSuccessHandler(url));
        return this;
    }

    public void init(H http) throws Exception {
        super.init(http);
    }

    private String getPhoneParam() {
        return this.getAuthenticationFilter().getPhoneParam();
    }

    private String getSmsCodeParam() {
        return this.getAuthenticationFilter().getSmsCodeParam();
    }

    public static SmsLoginConfigure<HttpSecurity> smsLogin() {
        return new SmsLoginConfigure<>();
    }

}

















