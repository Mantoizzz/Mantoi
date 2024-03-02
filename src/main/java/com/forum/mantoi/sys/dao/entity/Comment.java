package com.forum.mantoi.sys.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author DELL
 */
@TableName(value = "t_comment")
@Data
@Builder
public class Comment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "content")
    private String content;

    @TableField(value = "author_id")
    private Long authorId;

    @TableField(value = "likes")
    private Integer likes;

    @TableField(value = "post_id")
    private Long postId;

    @TableField(value = "parent_id")
    private Long parentId;

    @TableField(value = "publish_time", fill = FieldFill.INSERT)
    private Date publishTime;

}
