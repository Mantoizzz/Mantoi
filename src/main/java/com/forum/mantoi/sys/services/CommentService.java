package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.payload.CommentRequest;
import com.forum.mantoi.sys.entity.Comment;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class CommentService implements PublishService<Comment> {


    private final CommentRepository commentRepository;

    private final SensitiveWordService sensitiveWordService;

    @Override
    public Comment publish(User author, Object request) {
        CommentRequest commentRequest = (CommentRequest) request;
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setComments(commentRequest.getComment());
        comment.setPublishTime(commentRequest.getPublishTime());
        comment.setLikes(commentRequest.getLikes());
        comment.setCommentPost(commentRequest.getCommentPost());
        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Comment modify(User author, Comment object, Object request) {
        return null;
    }
}
