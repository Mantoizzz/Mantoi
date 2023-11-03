package com.forum.mantoi.sys.model;

import lombok.Data;

@Data
public class LoginUser {

    private String username;

    private String password;

    private Integer rememberMe;
}
