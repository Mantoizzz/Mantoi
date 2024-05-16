package com.forum.mantoi.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装逻辑类
 *
 * @author DELL
 */
@Data
public class RestResponse<T> implements Serializable {

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

    private RestResponse(ResultStatus resultStatus, String message) {
        this.resultStatus = resultStatus;
        this.message = message;
    }

    private RestResponse(T data, ResultStatus status, String msg) {
        this.data = data;
        this.resultStatus = status;
        this.message = status.getMsg();
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

    public static RestResponse<Void> fail(ResultStatus resultStatus, String msg) {
        return new RestResponse<>(resultStatus, msg);
    }

    public static RestResponse<Void> error() {
        return new RestResponse<>(CommonResultStatus.SERVER_ERROR);
    }

    public static <T> RestResponse<T> fail(T data, ResultStatus status) {
        return new RestResponse<>(data, status, status.getMsg());
    }

}
