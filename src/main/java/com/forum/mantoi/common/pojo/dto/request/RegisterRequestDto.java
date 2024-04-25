package com.forum.mantoi.common.pojo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DELL
 */
@Data
public class RegisterRequestDto implements Serializable {

    @NotBlank(message = "昵称不为空")
    public String username;

    @NotBlank(message = "邮箱不为空")
    @Email
    public String email;

    @NotBlank(message = "密码不能为空")
    public String password;

    @NotBlank(message = "手机号不为空")
    @Pattern(regexp = "^1[3|4|5|6|7|8|9][0-9]{9}$", message = "手机号格式不正确！")
    public String phone;

}
