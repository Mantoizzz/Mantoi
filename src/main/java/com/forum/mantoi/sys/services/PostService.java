package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.payload.PostRequest;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.PostRepository;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PostService implements PublishService<Post> {

    private final PostRepository postRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final Cache<String, Object> caffeineCache;

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
        postRepository.save(post);
        return post;
    }

    @Override
    public void delete(Long id) {
        Post deletePost = postRepository.findPostById(id).orElseThrow();
        postRepository.delete(deletePost);
    }

    @Override
    public Post modify(User author, Post object, Object request) {
        return null;
    }

    public void addLike(User user, Post post) {
        //TODO 使用Redis对点赞数进行存储
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}
