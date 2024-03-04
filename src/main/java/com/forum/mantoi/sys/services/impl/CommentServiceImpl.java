package com.forum.mantoi.sys.services.impl;

import com.forum.mantoi.common.pojo.dto.request.DeleteCommentDto;
import com.forum.mantoi.common.pojo.dto.request.PublishCommentDto;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.mapper.CommentMapper;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author DELL
 */
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public RestResponse<Void> publish(PublishCommentDto dto) {
        Comment comment = Comment.builder()
                .authorId(dto.getAuthor().getId())
                .content(dto.getContent())
                .likes(0)
                .postId(dto.getPostId())
                .parentId(dto.getParentId())
                .build();
        commentMapper.insert(comment);

        return RestResponse.ok();
    }

    @Override
    public RestResponse<Void> delete(DeleteCommentDto dto) {
        commentMapper.deleteById(dto.getCommentId());
        return RestResponse.ok();
    }

    @Override
    public Comment findById(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (Objects.isNull(comment)) {
            throw new BusinessException(CommonResultStatus.NULL, CommonResultStatus.NULL.getMsg());
        }
        return comment;
    }

    @Override
    public RestResponse<Void> save(Comment comment) {
        commentMapper.insert(comment);
        return RestResponse.ok();
    }

    @Override
    public List<Comment> findComments(Post post) {
        return commentMapper.selectCommentsByPostId(post.getId());
    }

    @Override
    public List<Comment> findReply(Comment comment) {
        return commentMapper.selectRepliesByCommentId(comment.getId());
    }

}
