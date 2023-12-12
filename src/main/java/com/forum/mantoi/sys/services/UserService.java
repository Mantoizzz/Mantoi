package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.common.payload.RegisterRequest;
import com.forum.mantoi.common.payload.UserInfoRequest;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * user 我自己
     * target 我要关注的人
     *
     * @param user   我
     * @param target 对方
     */
    @Transactional
    public void subscribe(User user, User target) {
        user.getSubscribers().add(target);
        target.getFollowers().add(user);
        userRepository.save(user);
        userRepository.save(target);
    }

    /**
     * 订阅功能的预处理
     *
     * @param userId   我的id
     * @param targetId 对方id
     * @param sub      true->关注;false->取关
     */
    public void processSubscribe(Long userId, Long targetId, boolean sub) {
        Optional<User> optional = userRepository.findById(userId);
        User user;
        User target;
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new UserException(CommonResultStatus.FAIL, "User does not exit");
        }
        optional = userRepository.findById(targetId);
        if (optional.isPresent()) {
            target = optional.get();
        } else {
            throw new UserException(CommonResultStatus.FAIL, "Target does not exit");
        }
        if (sub) {
            subscribe(user, target);
        } else {
            unSubscribe(user, target);
        }
    }

    /**
     * 取消订阅
     *
     * @param user   我
     * @param target 要取消的人
     */
    @Transactional
    public void unSubscribe(User user, User target) {
        user.getSubscribers().remove(target);
        target.getFollowers().remove(user);
        userRepository.save(user);
        userRepository.save(target);
    }

    /**
     * 修改个人信息
     *
     * @param userId          userId
     * @param userInfoRequest payload
     */
    @Transactional
    public void updateUserInfo(long userId, UserInfoRequest userInfoRequest) {
        Optional<User> updateUser = userRepository.findById(userId);
        if (updateUser.isPresent()) {
            userRepository.updateUser(
                    userInfoRequest.getUsername()
                    , userInfoRequest.getEmail()
                    , userInfoRequest.getIntroduction()
                    , userInfoRequest.getAvatar()
                    , userId
            );
        } else {
            throw new UserException(CommonResultStatus.FAIL, "User does not exit");
        }
    }

    /**
     * 用户登录
     *
     * @param request RegisterRequest
     * @return Map
     */
    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> map = new HashMap<>();

        if (request == null) {
            throw new IllegalArgumentException("Null Parameter");
        }

        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            map.put("usernameField", "The name is used already!");
            return map;
        }

        user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            map.put("emailField", "This email is already used!");
            return map;
        }

        //开始注册
        User regisUser = new User();
        regisUser.setUsername(request.getUsername());
        regisUser.setPassword(passwordEncoder.encode(request.getPassword()));
        regisUser.setEmail(request.getEmail());
        regisUser.setCreateTime(new Date());
        userRepository.save(regisUser);
        return map;

    }


}
