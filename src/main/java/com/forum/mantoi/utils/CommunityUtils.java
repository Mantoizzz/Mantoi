package com.forum.mantoi.utils;

import com.alibaba.fastjson.JSONObject;
import com.forum.mantoi.common.response.CommonResultStatus;

import java.util.Map;
import java.util.Objects;

/**
 * @author DELL
 */
public final class CommunityUtils {

    private CommunityUtils() {

    }

    public static String getJsonString(int code, String msg, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if (Objects.nonNull(map)) {
            jsonObject.putAll(map);
        }
        return jsonObject.toJSONString();
    }

    public static String getJsonString(CommonResultStatus commonResultStatus) {
        return getJsonString(commonResultStatus.getCode(), commonResultStatus.getMsg(), null);
    }

    /**
     * 计算帖子的score
     *
     * @param postId postId
     */
    public static void processScores(long postId) {
        int likes = (int) RedisUtils.hget(RedisKeys.getRankUpdatedPosts(), postId + ":likes");
        int comments = (int) RedisUtils.hget(RedisKeys.getRankUpdatedPosts(), postId + ":comments");
        int views = (int) RedisUtils.hget(RedisKeys.getRankUpdatedPosts(), postId + ":views");
        double increment = likes * (0.5) + comments * (0.35) + views * (0.01);
        RedisUtils.zsetIncrement(RedisKeys.getRankSet(), Long.toString(postId), increment);

        clearHash(postId);
    }

    private static void clearHash(long postId) {
        RedisUtils.hset(RedisKeys.getRankUpdatedPosts(), postId + ":likes", 0);
        RedisUtils.hset(RedisKeys.getRankUpdatedPosts(), postId + ":comments", 0);
        RedisUtils.hset(RedisKeys.getRankUpdatedPosts(), postId + ":views", 0);
    }


}
