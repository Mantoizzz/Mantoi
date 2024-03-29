package com.forum.mantoi.sys.quartz;

import com.alibaba.fastjson.JSON;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.utils.RedisKeys;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * 定时缓存热度最高的帖子的Job
 *
 * @author DELL
 */
@AllArgsConstructor
@Slf4j
public class TopPostsRefresh implements Job {

    private final PostService postService;

    private final Cache<String, Object> cache;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("Quartz:TopPostsRefresh Begin...");
        List<Post> topList = postService.getTopPosts();
        String key = RedisKeys.getTopPosts();
        try {
            cache.put(key, topList);
            String jsonList = JSON.toJSONString(topList);
            stringRedisTemplate.opsForValue().set(key, jsonList);
        } catch (Exception e) {
            log.error("Quartz:Error while refreshing top posts", e);
        }
        log.info("Quartz:TopPostsRefresh Done...");
    }

}
