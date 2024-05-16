package com.forum.mantoi.sys.services.impl;

import com.forum.mantoi.common.pojo.dto.request.DeleteCommentDto;
import com.forum.mantoi.common.pojo.dto.request.PublishCommentDto;
import com.forum.mantoi.common.pojo.vo.CommentVO;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.mapper.CommentMapper;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.CommentService;
import com.forum.mantoi.utils.RedisKeys;
import com.forum.mantoi.utils.RedisUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author DELL
 */
@Service("commentService")
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    /**
     * 1.创建Comment类，并更新到数据库
     * 2.把帖子id放进set里面，表明需要更新
     *
     * @param dto DTO
     * @return RestResponse
     */
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

        RedisUtils.hincr(RedisKeys.getRankUpdatedPosts(), dto.getPostId().toString() + ":Comments", 1);
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

    /**
     * 拿到这个评论的回复
     *
     * @param comment Comment
     * @return List of Reply
     */
    @Override
    public List<Comment> findReply(Comment comment) {
        return commentMapper.selectRepliesByCommentId(comment.getId());
    }

    /**
     * 拿到一个帖子的所有Comment和Reply
     *
     * @param post post
     * @return List
     * @see CommentVO
     */
    @Override
    public List<CommentVO> getPostComments(Post post) {
        List<CommentVO> resultList = new ArrayList<>();
        List<Comment> comments = this.findComments(post);
        for (Comment comment : comments) {
            List<Comment> replyList = this.findReply(comment);
            CommentVO commentVO = new CommentVO();
            commentVO.setParent(comment);
            commentVO.setReplies(replyList);
            resultList.add(commentVO);
        }
        return resultList;
    }

}
