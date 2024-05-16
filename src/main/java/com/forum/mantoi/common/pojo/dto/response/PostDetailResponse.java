package com.forum.mantoi.common.pojo.dto.response;

import com.forum.mantoi.common.pojo.vo.CommentVO;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.model.SysUser;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author DELL
 */
@Data
@Builder
public class PostDetailResponse implements Serializable {

    Post post;

    SysUser curUser;

    User author;

    long likes;

    boolean isLiked;

    List<CommentVO> commentVOList;

}
