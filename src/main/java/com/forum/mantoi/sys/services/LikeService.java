package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.constant.Entity;

/**
 * @author DELL
 */
public interface LikeService {

    void addLike(long userId, long objId, Entity obj);

    long viewLikes(Entity obj, long objId);

    boolean isLiked(Entity obj, long objId, long userId);


}
