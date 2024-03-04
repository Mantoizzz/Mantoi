package com.forum.mantoi.common.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaVO implements Serializable {

    private String id;

    private String base64;

}
