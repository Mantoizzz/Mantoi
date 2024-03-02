package com.forum.mantoi.common.pojo.request;

import com.forum.mantoi.sys.dao.entity.User;
import lombok.Builder;
import lombok.Data;

/**
 * @author DELL
 */
@Data
public class PublishCommentDto {

    private String content;

    private User author;

    private Long postId;

    private Long parentId;

}
