package com.forum.mantoi.utils;

import com.forum.mantoi.sys.model.Entity;

/**
 * 用于给存入redis的key取名的
 */
public class RedisKeys {

    private static final String ENTITY_LIKE_SET_PREFIX = "likeSet:entity:";

    private static final String ENTITY_LIKE_COUNT_PREFIX = "likeCount:entity:";

    private static final String SPLIT = ":";

    private static final String BLACK_TOKEN_PREFIX = "blackListToken";

    private static final String WEBSOCKET_LIST_PREFIX = "websocket:";

    private static final String POST_SCORE_SET = "post:score";

    public static String getEntityLikeSetKey(Entity entity, Long id) {
        return ENTITY_LIKE_SET_PREFIX + entity.toString().toLowerCase() + SPLIT + id;
    }

    public static String getEntityLikeCountKey(Entity entity, Long id) {
        return ENTITY_LIKE_COUNT_PREFIX + entity.toString().toLowerCase() + SPLIT + id;
    }

    public static String getBlackListTokenKey(String username) {
        return BLACK_TOKEN_PREFIX + SPLIT + username;
    }

    public static String getWebsocketListKey(String email) {
        return WEBSOCKET_LIST_PREFIX + email;
    }

    public static String getPostScoreSet() {
        return POST_SCORE_SET;
    }

}
