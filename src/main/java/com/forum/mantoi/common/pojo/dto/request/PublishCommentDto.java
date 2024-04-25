package com.forum.mantoi.common.pojo.dto.request;

import com.forum.mantoi.sys.dao.entity.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DELL
 */
@Data
public class PublishCommentDto implements Serializable {

    private String content;

    private User author;

    private Long postId;

    private Long parentId;

}
