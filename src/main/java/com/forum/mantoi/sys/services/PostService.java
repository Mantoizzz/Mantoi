package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.payload.PostRequest;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.PostingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@AllArgsConstructor
public class PostService implements PublishService<Post> {

    private final PostingRepository postingRepository;

    @Override
    public Post publish(User author, Object request) {
        PostRequest postRequest = (PostRequest) request;
        Post post = Post.builder()
                .author(author)
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .publishTime(postRequest.getPublishTime())
                .commentPosts(new ArrayList<>())
                .likes(0)
                .build();
        postingRepository.save(post);
        return post;
    }

    @Override
    public void delete(Long id) {
        Post deletePost = postingRepository.findPostById(id).orElseThrow();
        postingRepository.delete(deletePost);
    }

    @Override
    public Post modify(User author, Post object, Object request) {
        return null;
    }
}
