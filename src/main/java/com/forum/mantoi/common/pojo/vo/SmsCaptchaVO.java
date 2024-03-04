package com.forum.mantoi.common.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author DELL
 */
@Data
public class SmsCaptchaVO implements Serializable {

    private String phone;

    private int expire;
}
