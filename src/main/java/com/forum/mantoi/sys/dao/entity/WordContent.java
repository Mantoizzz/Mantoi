package com.forum.mantoi.sys.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "t_word_content")
public class WordContent {
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    @TableField(value = "word_id")
    private long wordId;

    @TableField(value = "post_content_id")
    private long postId;
}
