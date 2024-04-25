package com.forum.mantoi.common.pojo.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author DELL
 */
@Data
public class LoginRequestDto implements Serializable {

    private String username;

    private String password;

}
