package com.forum.mantoi.sys.services;

import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.sys.services.impl.UserServiceImpl;
import com.forum.mantoi.utils.RedisKeys;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class FollowService {

    private final UserServiceImpl userServiceImpl;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 关注某个实体
     *
     * @param userId   user
     * @param entity   entity
     * @param entityId id
     */
//    public void follow(long userId, Entity entity, long entityId) {
//        redisTemplate.execute(new SessionCallback<>() {
//            @Override
//            public Object execute(@NonNull RedisOperations operations) throws DataAccessException {
//                String subscribeKey = RedisKeys.getSubscribersKey(userId, entity);
//                String followerKey = RedisKeys.getFollowerKey(entity, entityId);
//
//                operations.multi();
//                operations.opsForZSet().add(subscribeKey, entityId, System.currentTimeMillis());
//                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
//
//                return operations.exec();
//            }
//        });
//
//    }

    /**
     * 对某个实体取消关注
     *
     * @param userId   user
     * @param entity   entity
     * @param entityId id
     */
    public void unfollow(long userId, Entity entity, long entityId) {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(@NonNull RedisOperations operations) throws DataAccessException {
                String subscribeKey = RedisKeys.getSubscribersKey(userId, entity);
                String followerKey = RedisKeys.getFollowerKey(entity, entityId);

                operations.multi();

                operations.opsForZSet().remove(subscribeKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });
    }

    /**
     * 查询某个用户关注了多少
     *
     * @param userId userId
     * @param entity entity
     * @return Count
     */
    public long findSubscribersCount(Entity entity, long userId) {
        String subscribersKey = RedisKeys.getSubscribersKey(userId, entity);
        return redisTemplate.opsForZSet().zCard(subscribersKey);
    }

    /**
     * 查询实体粉丝
     *
     * @param entity   entity
     * @param entityId id
     * @return Count
     */
    public long findFollowerCount(Entity entity, long entityId) {
        String followersKey = RedisKeys.getFollowerKey(entity, entityId);
        return redisTemplate.opsForZSet().zCard(followersKey);
    }

    /**
     * 是否已经关注实体
     *
     * @param userId   user
     * @param entity   entity
     * @param entityId id
     * @return bool
     */
    public boolean hasFollowed(long userId, Entity entity, long entityId) {
        String subscribersKey = RedisKeys.getSubscribersKey(userId, entity);
        return redisTemplate.opsForZSet().score(subscribersKey, entityId) != null;
    }

    /**
     * 查询某人关注的人
     *
     * @param userId user
     * @param start  分页start
     * @param size   分页size
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> findSubscribers(long userId, int start, int size) {
        String subscribersKey = RedisKeys.getSubscribersKey(userId, Entity.USER);
        return find(subscribersKey, start, start + size - 1);
    }

    /**
     * 查询某人的粉丝
     *
     * @param userId user
     * @param start  start
     * @param size   size
     */
    public List<Map<String, Object>> findFollowers(long userId, int start, int size) {
        String followersKey = RedisKeys.getFollowerKey(Entity.USER, userId);
        return find(followersKey, start, start + size - 1);
    }

    /**
     * 范围查询
     *
     * @param key   redisKey
     * @param start start
     * @param end   end
     * @return List
     */
    private List<Map<String, Object>> find(String key, int start, int end) {
        Set<Object> targetSet = redisTemplate.opsForZSet().range(key, start, end);
        if (targetSet == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Object obj : targetSet) {
            Map<String, Object> map = new HashMap<>();
            User user = userServiceImpl.findUserById((Long) obj);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(key, obj);
            if (score != null) {
                map.put("followTime", new Date(score.longValue()));
            }
            list.add(map);
        }
        return list;
    }
}
