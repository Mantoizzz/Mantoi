package com.forum.mantoi.common.pojo.vo;

import com.forum.mantoi.sys.dao.entity.Comment;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author DELL
 */
@Data
public class CommentVO implements Serializable {

    private Comment parent;

    List<Comment> replies;

}
