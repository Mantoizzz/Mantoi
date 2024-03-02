package com.forum.mantoi.sys.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @author DELL
 */
@Data
@Builder
@TableName(value = "t_post_content")
public class PostContent {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "post_id")
    private Long postId;

    @TableField(value = "content")
    private String content;

}
