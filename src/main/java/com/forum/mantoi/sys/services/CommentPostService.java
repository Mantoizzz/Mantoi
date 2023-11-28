package com.forum.mantoi.sys.services;


import com.forum.mantoi.common.payload.CommentPostRequest;
import com.forum.mantoi.sys.entity.CommentPost;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.CommentPostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@AllArgsConstructor
public class CommentPostService implements PublishService<CommentPost> {

    private final CommentPostRepository commentPostRepository;


    @Override
    public CommentPost publish(User author, Object request) {
        CommentPostRequest commentPostRequest = (CommentPostRequest) request;
        CommentPost commentPost = CommentPost.builder()
                .author(author)
                .publishTime(new Date())
                .likes(0)
                .comments(new ArrayList<>())
                .content(commentPostRequest.getContent())
                .post(commentPostRequest.getPost())
                .build();
        commentPostRepository.save(commentPost);
        return commentPost;
    }

    @Override
    public void delete(Long id) {
        CommentPost commentPost = commentPostRepository.findById(id).orElseThrow();
        commentPostRepository.delete(commentPost);
    }

    @Override
    public CommentPost modify(User author, CommentPost object, Object request) {
        return null;
    }
}
