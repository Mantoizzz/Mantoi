package com.forum.mantoi.sys.services.impl;

import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.utils.RedisKeys;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * 点赞Service
 *
 * @author DELL
 */
@Service("likeService")
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 给实体点赞
     *
     * @param userId    点赞人的id
     * @param objId     对象的id
     * @param objEntity 实体(枚举类)
     */
    @Override
    public void addLike(long userId, long objId, Entity objEntity) {

        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(@NonNull RedisOperations<K, V> operations) throws DataAccessException {
                String entityLikeSetKeyName = RedisKeys.getEntityLikeSetKey(objEntity, objId);
                String entityLikeCountKeyName = RedisKeys.getEntityLikeCountKey(objEntity, objId);
                boolean isLiked = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(entityLikeSetKeyName, userId));

                //开启事务
                operations.multi();
                if (isLiked) {
                    redisTemplate.opsForSet().remove(entityLikeSetKeyName, userId);
                    redisTemplate.opsForValue().decrement(entityLikeCountKeyName);
                } else {
                    redisTemplate.opsForSet().add(entityLikeSetKeyName, userId);
                    redisTemplate.opsForValue().increment(entityLikeCountKeyName);
                }
                return operations.exec();
            }
        });
    }

    /**
     * 查看点赞数
     *
     * @param entity   实体(枚举类)
     * @param entityId 实体id
     * @return 点赞数
     */
    @Override
    public long viewLikes(Entity entity, long entityId) {
        String entityLikeCountKeyName = RedisKeys.getEntityLikeCountKey(entity, entityId);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(entityLikeCountKeyName))) {
            return (long) redisTemplate.opsForValue().get(entityLikeCountKeyName);
        }
        throw new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "Likes does not exits");
    }

    /**
     * 是否已点赞
     *
     * @param entity   实体(枚举类)
     * @param entityId 实体id
     * @param userId   用户id
     * @return boolean
     */
    @Override
    public boolean isLiked(Entity entity, long entityId, long userId) {
        String entityLikeSetKeyName = RedisKeys.getEntityLikeSetKey(entity, entityId);
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(entityLikeSetKeyName, userId));
    }

}
