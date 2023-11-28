package com.forum.mantoi.common.payload;

import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class CommentPostRequest {

    public Post post;

    public String content;

    public User author;

    public Date date;

}
