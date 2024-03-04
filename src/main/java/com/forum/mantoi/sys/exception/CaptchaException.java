package com.forum.mantoi.sys.exception;

import com.forum.mantoi.common.response.ResultStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * @author DELL
 */
public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CaptchaException(String msg) {
        super(msg);
    }

    public CaptchaException(ResultStatus status, String msg) {
        super(msg);
    }
}
