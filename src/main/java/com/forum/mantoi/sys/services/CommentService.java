package com.forum.mantoi.sys.services;


import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.sys.entity.Comment;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.repository.CommentRepository;
import com.forum.mantoi.utils.RedisKeys;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService implements PublishService<Comment> {

    private final CommentRepository commentRepository;

    private final PostService postService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Comment publish(Comment object) {
        commentRepository.save(object);
        String redisKey = RedisKeys.getPostScoreSet();
        redisTemplate.opsForSet().add(redisKey, object.getPost().getId());
        return object;
    }


    /**
     * 删除评论包括删除子评论和自己
     *
     * @param id 删除的id
     */
    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "comment does not exist"));
        List<Comment> comments = comment.getComments();
        if (comments.isEmpty()) {
            commentRepository.delete(comment);
            return;
        }
        for (Comment sonComment : comments) {
            delete(sonComment.getId());
        }
    }

    /**
     * 获得某个实体的Comment
     *
     * @param entity 实体(枚举类)
     * @param id     ID
     * @return List
     */
    public List<Comment> findCommentsById(Entity entity, long id) {
        if (entity == Entity.POST) {
            Post post = postService.findById(id).orElseThrow(()
                    -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "post does not exist"));
            return post.getComments();
        } else if (entity == Entity.COMMENT) {
            Comment comment = findById(id).orElseThrow(()
                    -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "comment does not exist"));
            return comment.getComments();
        } else {
            throw new BusinessException(CommonResultStatus.PARAM_ERROR, "Only POST or COMMENT permitted");
        }
    }

    /**
     * 返回null表明该评论没有父评论
     *
     * @param comment comment
     * @return 父评论
     */
    public Comment findParent(Comment comment) {
        if (comment.getParent() == -1) {
            return null;
        }
        return commentRepository.findById(comment.getParent()).orElseThrow(() ->
                new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "parent does not exist"));
    }


    public void addComment(Comment parentComment, Comment newComment) {
        parentComment.getComments().add(newComment);
        commentRepository.save(parentComment);
    }

    public Optional<Comment> findById(long commentId) {
        return commentRepository.findById(commentId);
    }

}