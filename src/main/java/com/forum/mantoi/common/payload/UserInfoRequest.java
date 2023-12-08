package com.forum.mantoi.common.payload;

import lombok.Data;

@Data
public class UserInfoRequest {

    String username;

    String email;

    String introduction;

    String avatar;

}
