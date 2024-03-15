package com.forum.mantoi.sys.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_word")
public class Word {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "word")
    private String word;

}
