package com.forum.mantoi.sys.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DELL
 */
@Data
@Builder
@TableName(value = "t_post")
public class Post implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "title")
    private String title;

    @TableField(value = "author_id")
    private Long authorId;

    @TableField(value = "short_content")
    private String shortContent;

    @TableField(value = "likes")
    private Integer likes;

    @TableField(fill = FieldFill.INSERT, value = "publish_time")
    private Date publishTime;

    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private Date updateTime;

    @TableField(value = "score")
    private Double score;

}
