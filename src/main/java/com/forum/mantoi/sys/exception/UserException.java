package com.forum.mantoi.sys.exception;

import com.forum.mantoi.common.response.ResultStatus;

public class UserException extends BusinessException {

    public UserException(ResultStatus status) {
        super(status);
    }

    public UserException(ResultStatus status, String message) {
        super(status, message);
    }
}
