package com.forum.mantoi.common.response;

import lombok.Data;

/**
 * 封装逻辑类
 *
 * @author DELL
 */
@Data
public class RestResponse<T> {

    private final ResultStatus resultStatus;

    private final String message;

    private T data;

    private RestResponse() {
        this.resultStatus = CommonResultStatus.OK;
        this.message = CommonResultStatus.OK.getMsg();
    }

    private RestResponse(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
        this.message = resultStatus.getMsg();
    }

    private RestResponse(T data) {
        this();
        this.data = data;
    }

    public static RestResponse<Void> ok() {
        return new RestResponse<>();
    }

    public static <T> RestResponse<T> ok(T data) {
        return new RestResponse<>(data);
    }

    public static RestResponse<Void> fail(ResultStatus resultStatus) {
        return new RestResponse<>(resultStatus);
    }

    public static RestResponse<Void> error() {
        return new RestResponse<>(CommonResultStatus.SERVER_ERROR);
    }

}
