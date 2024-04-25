package com.forum.mantoi.sys.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.mantoi.common.constant.DataBaseConstants;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.common.pojo.dto.response.RegisterResponseDto;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.pojo.dto.request.RegisterRequestDto;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.CommentMapper;
import com.forum.mantoi.sys.dao.mapper.PostMapper;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.Role;
import com.forum.mantoi.sys.model.SysUser;
import com.forum.mantoi.sys.services.UserService;
import com.forum.mantoi.utils.JwtUtils;
import com.forum.mantoi.utils.RedisKeys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author DELL
 */
@Service("userService")
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final PostMapper postMapper;

    private final CommentMapper commentMapper;

    private final UserMapper userMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Register
     *
     * @param dto DTO
     * @return Response
     */
    @Override

    public RestResponse<RegisterResponseDto> register(RegisterRequestDto dto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DataBaseConstants.UserTable.COLUMN_USERNAME, dto.getUsername())
                .or()
                .eq(DataBaseConstants.UserTable.COLUMN_EMAIL, dto.getEmail())
                .or()
                .eq(DataBaseConstants.UserTable.COLUMN_PHONE, dto.getPhone())
                .last(DataBaseConstants.SqlEnum.LIMIT_1.getSql());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(CommonResultStatus.FAIL, "用户已存在");
        }
        String password = passwordEncoder.encode(dto.getPassword());
        User user = User.builder()
                .password(password)
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .role(Role.USER)
                .avatar("")
                .introduction("这个人暂时还没有自我介绍")
                .build();
        userMapper.insert(user);

        return RestResponse.ok(RegisterResponseDto.builder()
                .token(JwtUtils.generateToken(dto.getEmail(), Role.USER.getAuthorities()))
                .build());
    }

    /**
     * 根据ID返回User
     * 如果User不存在则报错
     *
     * @param id id
     * @return User
     * @see BusinessException
     */
    @Override
    public User findUserById(Long id) {
        User user = userMapper.selectById(id);
        if (Objects.isNull(user)) {
            throw new BusinessException(CommonResultStatus.NULL, "User not found");
        }
        return user;
    }

    @Override
    public long getUserLikes(Long id) {
        long result = 0;
        for (Post post : getUserPosts(id)) {
            result += post.getLikes();
        }

        for (Comment comment : getUserComments(id)) {
            result += comment.getLikes();
        }
        return result;
    }

    @Override
    public List<Post> getUserPosts(Long id) {
        return postMapper.findPostsByUserId(id);
    }

    @Override
    public List<Comment> getUserComments(Long id) {
        return commentMapper.selectCommentsByUserId(id);
    }

    @Override
    public User convert(SysUser sysUser) {
        Long id = sysUser.getId();
        User user = userMapper.selectById(id);
        if (Objects.isNull(user)) {
            throw new UserException(CommonResultStatus.RECORD_NOT_EXIST, "user not found");
        }
        return user;
    }

    /**
     * 关注功能：
     * 给每个用户维护两个set，一个是用户关注的set，一个是关注用户的set
     * 关注就是在前者加一个目标的id，然后在后者加一个用户id
     *
     * @param userId user
     * @param obj    obj
     * @param objId  id
     */
    @Override
    public void follow(long userId, Entity obj, long objId) {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(@NonNull RedisOperations operations) throws DataAccessException {
                String subscribeKey = RedisKeys.getSubscribersKey(userId, obj);
                String followerKey = RedisKeys.getFollowerKey(obj, objId);

                operations.multi();
                operations.opsForZSet().add(subscribeKey, objId, System.currentTimeMillis());
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    /**
     * @param userId user
     * @param obj    obj
     * @param objId  id
     */
    @Override
    public void unfollow(long userId, Entity obj, long objId) {

        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String subscribeKey = RedisKeys.getSubscribersKey(userId, obj);
                String followerKey = RedisKeys.getFollowerKey(obj, objId);

                operations.multi();

                operations.opsForZSet().remove(subscribeKey, objId);
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });
    }

    /**
     * 查询用户关注的obj关注了多少
     *
     * @param obj    obj
     * @param userId user
     * @return count
     */
    @Override
    public long findSubscribeCount(Entity obj, long userId) {
        String subscribersKey = RedisKeys.getSubscribersKey(userId, obj);
        return redisTemplate.opsForZSet().zCard(subscribersKey);
    }

    /**
     * 查询用户的粉丝
     *
     * @param userId user
     * @return count
     */
    @Override
    public long getUserFansCount(long userId) {
        String followersKey = RedisKeys.getFollowerKey(Entity.USER, userId);
        return redisTemplate.opsForZSet().zCard(followersKey);
    }

    /**
     * 判断是否已经关注
     *
     * @param userId user
     * @param obj    obj
     * @param objId  id
     * @return boolean
     */
    @Override
    public boolean hasFollowed(long userId, Entity obj, long objId) {
        String subscribersKey = RedisKeys.getSubscribersKey(userId, obj);
        return redisTemplate.opsForZSet().score(subscribersKey, objId) != null;
    }

    /**
     * 查询粉丝List
     *
     * @param userId user
     * @param start  start
     * @param size   size
     * @return List
     */
    @Override
    public List<Map<String, Object>> findFans(long userId, int start, int size) {
        String followersKey = RedisKeys.getFollowerKey(Entity.USER, userId);
        return find(followersKey, start, start + size - 1);
    }

    /**
     * 查询用户关注的实体List
     *
     * @param userId user
     * @param start  start
     * @param size   size
     * @param obj    obj
     * @return List
     */
    @Override
    public List<Map<String, Object>> findSubscribers(long userId, int start, int size, Entity obj) {
        String subscribersKey = RedisKeys.getSubscribersKey(userId, obj);
        return find(subscribersKey, start, start + size - 1);
    }


    private List<Map<String, Object>> find(String key, int start, int end) {
        Set<Object> targetSet = redisTemplate.opsForZSet().range(key, start, end);
        if (targetSet == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Object obj : targetSet) {
            Map<String, Object> map = new HashMap<>();
            User user = findUserById((Long) obj);
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
