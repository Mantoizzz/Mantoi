package com.forum.mantoi.utils;

import com.forum.mantoi.sys.model.Entity;

/**
 * 用于给存入redis的key取名的
 */
public class RedisKeys {

    private static final String ENTITY_LIKE_SET_PREFIX = "likeSet:entity:";

    private static final String ENTITY_LIKE_COUNT_PREFIX = "likeCount:entity:";

    private static final String SPLIT = ":";

    public static String getEntityLikeSetKey(Entity entity, Long id) {
        return ENTITY_LIKE_SET_PREFIX + entity.toString().toLowerCase() + SPLIT + id;
    }

    public static String getEntityLikeCountKey(Entity entity, Long id) {
        return ENTITY_LIKE_COUNT_PREFIX + entity.toString().toLowerCase() + SPLIT + id;
    }
}
