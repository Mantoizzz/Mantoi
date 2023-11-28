package com.forum.mantoi.common.payload;

import lombok.Data;

@Data
public class RegisterRequest {

    public String username;

    public String email;

    public String password;

}
