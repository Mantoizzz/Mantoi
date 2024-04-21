package com.forum.mantoi.sys.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.forum.mantoi.sys.model.Role;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mantoi
 */
@Data
@Builder
@TableName(value = "t_user")
public class User implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "email")
    private String email;

    @TableField(value = "introduction")
    private String introduction;

    @TableField(value = "likes")
    private long likes;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "role")
    private Role role;

    @TableField(value = "gender")
    private String gender;

}
