package com.forum.mantoi.sys.exception;

import com.forum.mantoi.common.response.ResultStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private final ResultStatus status;

    public BusinessException(ResultStatus status, String message) {
        super(message);
        this.status = status;
    }

}
