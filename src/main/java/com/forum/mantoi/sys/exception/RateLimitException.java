package com.forum.mantoi.sys.exception;

import com.forum.mantoi.common.response.ResultStatus;

/**
 * @author DELL
 */
public class RateLimitException extends BusinessException {

    public RateLimitException(ResultStatus status) {
        super(status);
    }

    public RateLimitException(ResultStatus status, String message) {
        super(status, message);
    }
}
