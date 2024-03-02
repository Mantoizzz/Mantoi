package com.forum.mantoi.common.pojo.response;

import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author DELL
 */
@Data
@Builder
public class PostDetailResponse implements Serializable {

    Post post;

    User curUser;

    User author;

    long likes;

    boolean isLiked;

    List<Map<String, Object>> comments;

}
