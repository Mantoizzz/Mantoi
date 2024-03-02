package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.pojo.request.DeleteCommentDto;
import com.forum.mantoi.common.pojo.request.PublishCommentDto;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;

import java.util.List;


/**
 * @author DELL
 */
public interface CommentService {

    RestResponse<Void> publish(PublishCommentDto dto);

    RestResponse<Void> delete(DeleteCommentDto dto);

    Comment findById(Long id);

    RestResponse<Void> save(Comment comment);

    List<Comment> findComments(Post post);

    List<Comment> findReply(Comment comment);
}
