package com.forum.mantoi.common.payload;

import com.forum.mantoi.sys.entity.Comment;
import com.forum.mantoi.sys.entity.CommentPost;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentRequest {

    String content;

    CommentPost commentPost;

    List<Comment> comment;

    Integer likes;

    Date publishTime;

}
