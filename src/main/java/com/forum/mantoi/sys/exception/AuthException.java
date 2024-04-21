package com.forum.mantoi.sys.exception;

import com.forum.mantoi.common.response.ResultStatus;

/**
 * @author DELL
 */
public class AuthException extends BusinessException {

    public AuthException(ResultStatus status) {
        super(status);
    }

    public AuthException(ResultStatus status, String message) {
        super(status, message);
    }

}
