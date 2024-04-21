package com.forum.mantoi.sys.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DELL
 */
@TableName(value = "t_word_content")
@Data
public class WordContent implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    @TableField(value = "word_id")
    private long wordId;

    @TableField(value = "post_content_id")
    private long postId;
}
