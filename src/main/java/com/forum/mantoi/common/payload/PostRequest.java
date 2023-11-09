package com.forum.mantoi.common.payload;

import com.forum.mantoi.sys.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostRequest extends PublishRequest {

    public String content;

    public User author;

    public Date publishTime = new Date();

    public String title;

}
