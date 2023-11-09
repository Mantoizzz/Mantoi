package com.forum.mantoi.common.payload;

import com.forum.mantoi.sys.entity.User;

import java.util.Date;

public abstract class PublishRequest {

    public String content;

    public User author;

    public Date publishTime;


}
