package com.forum.mantoi.sys.exception;

import com.forum.mantoi.common.ResultStatus;

public class TokenExpiredException extends BusinessException {

    public TokenExpiredException(ResultStatus status) {
        super(status);
    }

    public TokenExpiredException(ResultStatus status, String message) {
        super(status, message);
    }

}
