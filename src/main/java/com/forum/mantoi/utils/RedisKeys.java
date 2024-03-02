package com.forum.mantoi.utils;

import com.forum.mantoi.common.constant.Entity;

/**
 * 用于给存入redis的key取名的
 *
 * @author DELL
 */
public class RedisKeys {

    private static final String ENTITY_LIKE_SET_PREFIX = "likeSet:entity:";

    private static final String ENTITY_LIKE_COUNT_PREFIX = "likeCount:entity:";

    private static final String SPLIT = ":";

    private static final String BLACK_TOKEN_PREFIX = "blackListToken";

    private static final String WEBSOCKET_LIST_PREFIX = "websocket:";

    private static final String POST_SCORE_SET = "post:score";

    private static final String TOP_POSTS = "topList";

    private static final String ALL_POSTS = "posts";

    private static final String SUBSCRIBERS_PREFIX = "subscribers:";

    private static final String FOLLOWER_PREFIX = "followers:";

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

    public static String getTopPosts() {
        return TOP_POSTS;
    }

    public static String getAllPosts() {
        return ALL_POSTS;
    }

    /**
     * 用户关注实体表的Key
     *
     * @param userId user
     * @param entity 实体的种类
     * @return Key
     */
    public static String getSubscribersKey(long userId, Entity entity) {
        return SUBSCRIBERS_PREFIX + entity.toString() + SPLIT + userId;
    }

    /**
     * 某个实体的粉丝表的key
     *
     * @param entity entity
     * @param id     id
     * @return Key
     */
    public static String getFollowerKey(Entity entity, long id) {
        return FOLLOWER_PREFIX + entity.toString() + SPLIT + id;
    }


}
