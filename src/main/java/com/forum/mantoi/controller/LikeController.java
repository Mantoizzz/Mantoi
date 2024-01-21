package com.forum.mantoi.controller;

import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.utils.CommunityUtil;
import com.forum.mantoi.utils.RedisKeys;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;

    private final RedisTemplate<String, Object> redisTemplate;

    @ResponseBody
    @RequestMapping(path = "/like", method = RequestMethod.POST)
    public String like(Entity entity, long id, long userId) {
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        likeService.addLike(userId, id, entity);
        long likeCount = likeService.viewLikes(entity, id);
        boolean isLiked = likeService.isLiked(entity, id, userId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("isLiked", isLiked);

        if (isLiked) {
            //TODO:触发点赞事件

            if (entity == Entity.POST) {
                String key = RedisKeys.getPostScoreSet();
                redisTemplate.opsForSet().add(key, id);
            }
        }
        return CommunityUtil.getJsonString(0, null, map);
    }
}
