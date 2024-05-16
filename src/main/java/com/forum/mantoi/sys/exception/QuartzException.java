package com.forum.mantoi.sys.exception;

import com.forum.mantoi.common.response.ResultStatus;

/**
 * @author DELL
 */
public class QuartzException extends BusinessException {

    public QuartzException(ResultStatus status) {
        super(status);
    }

    public QuartzException(ResultStatus status, String message) {
        super(status, message);
    }
}
