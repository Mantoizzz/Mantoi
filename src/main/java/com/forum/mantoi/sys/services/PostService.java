package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.repository.PostRepository;
import com.forum.mantoi.utils.RedisKeys;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 帖子Service
 */
@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PostService implements PublishService<Post> {

    private final PostRepository postRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final LikeService likeService;

    private final SensitiveWordService sensitiveWordService;


    @PostConstruct
    public void init() {

    }

    @Override
    public Post publish(Post object) {
        if (object == null) {
            throw new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "post does not exist");
        }
        object.setTitle(sensitiveWordService.replace(object.getTitle()));
        object.setContent(sensitiveWordService.replace(object.getContent()));
        postRepository.save(object);
        String redisKey = RedisKeys.getPostScoreSet();
        redisTemplate.opsForSet().add(redisKey, object.getId());
        return object;
    }

    @Override
    public void delete(Long id) {
        Post deletePost = postRepository.findPostById(id).orElseThrow();
        postRepository.delete(deletePost);
    }


    public List<Post> getTopPosts() {
        return postRepository.findTop25ByOrderByScoreDesc();
    }

    public void addLike(User user, Post post) {
        likeService.addLike(user.getId(), post.getId(), Entity.POST);
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    public void updateScore(Long postId, double score) {
        postRepository.updatePostScore(postId, score);
    }

    public void save(Post post) {
        postRepository.save(post);
    }
}
