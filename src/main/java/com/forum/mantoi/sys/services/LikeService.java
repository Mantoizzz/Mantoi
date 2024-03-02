package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.constant.Entity;

/**
 * @author DELL
 */
public interface LikeService {

    public void addLike(long userId, long objId, Entity obj);

    public long viewLikes(Entity obj, long objId);

    public boolean isLiked(Entity obj, long objId, long userId);


}
