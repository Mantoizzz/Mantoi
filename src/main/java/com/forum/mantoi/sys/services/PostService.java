package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.repository.PostRepository;
import com.forum.mantoi.utils.RedisKeys;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    private final Cache<String, Object> caffeineCache;

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

    public List<Post> findTopPosts() {
        return (List<Post>) caffeineCache.asMap().get(RedisKeys.getTopPosts());
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Optional<Post> findById(Long postId) {
        //先查询缓存
        List<Post> topList = findTopPosts();
        for (Post post : topList) {
            if (Objects.equals(post.getId(), postId)) {
                return Optional.of(post);
            }
        }
        return postRepository.findById(postId);
    }

    public void updateScore(Long postId, double score) {
        postRepository.updatePostScore(postId, score);
    }

    public void save(Post post) {
        postRepository.save(post);
    }
}
