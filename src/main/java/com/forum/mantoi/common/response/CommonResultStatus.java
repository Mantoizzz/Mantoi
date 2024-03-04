package com.forum.mantoi.common.response;

import lombok.AllArgsConstructor;

/**
 * @author DELL
 */

@AllArgsConstructor
public enum CommonResultStatus implements ResultStatus {

    OK(1000, "成功"),

    FAIL(1001, "失败"),

    PARAM_ERROR(1002, "参数非法"),

    RECORD_NOT_EXIST(1003, "记录不存在"),

    UNAUTHORIZED(1004, "未授权"),

    FORBIDDEN(1005, "禁止访问"),

    SERVER_ERROR(-1, "服务器内部错误"),

    NULL(1006, "值不可以为空");

    private final int code;

    private final String msg;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
    }