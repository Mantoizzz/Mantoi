package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.common.pojo.dto.request.RegisterRequestDto;
import com.forum.mantoi.common.pojo.dto.response.RegisterResponseDto;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author DELL
 */
public interface UserService {

    RestResponse<RegisterResponseDto> register(RegisterRequestDto dto);

    User findUserById(Long id);

    long getUserLikes(Long id);

    List<Post> getUserPosts(Long id);

    List<Comment> getUserComments(Long id);

    /**
     * 关注某个实体
     *
     * @param userId user
     * @param obj    obj
     * @param objId  id
     */
    void follow(long userId, Entity obj, long objId);


    /**
     * 取消关注某个实体
     *
     * @param userId user
     * @param obj    obj
     * @param objId  id
     */
    void unfollow(long userId, Entity obj, long objId);


    /**
     * 对于某种实体，用户关注了多少
     *
     * @param obj    obj
     * @param userId user
     * @return count
     */
    long findSubscribeCount(Entity obj, long userId);

    /**
     * 查询用户粉丝数
     *
     * @param userId user
     * @return count
     */
    long getUserFansCount(long userId);

    /**
     * 是否已经关注用户
     *
     * @param userId user
     * @param obj    obj
     * @param objId  id
     * @return boolean
     */
    boolean hasFollowed(long userId, Entity obj, long objId);

    /**
     * 查询某人粉丝
     *
     * @param userId user
     * @param start  start
     * @param size   size
     * @return list
     */
    List<Map<String, Object>> findFans(long userId, int start, int size);


    /**
     * 查询某人关注的人
     *
     * @param userId user
     * @param start  start
     * @param size   size
     * @return list
     */
    List<Map<String, Object>> findSubscribers(long userId, int start, int size, Entity obj);


}
