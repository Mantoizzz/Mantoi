package com.forum.mantoi.sys.services;


import com.forum.mantoi.common.payload.CommentPostRequest;
import com.forum.mantoi.sys.entity.Comment;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@AllArgsConstructor
public class CommentService implements PublishService<Comment> {

    private final CommentRepository commentRepository;


    @Override
    public Comment publish(User author, Object request) {
        CommentPostRequest commentPostRequest = (CommentPostRequest) request;
        Comment comment = Comment.builder()
                .author(author)
                .publishTime(new Date())
                .likes(0)
                .comments(new ArrayList<>())
                .content(commentPostRequest.getContent())
                .post(commentPostRequest.getPost())
                .build();
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        commentRepository.delete(comment);
    }

    @Override
    public Comment modify(User author, Comment object, Object request) {
        return null;
    }
}
