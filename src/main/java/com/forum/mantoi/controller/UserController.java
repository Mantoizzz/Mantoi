package com.forum.mantoi.controller;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.common.pojo.dto.response.UserProfileDto;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.SearchService;
import com.forum.mantoi.sys.services.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apiguardian.api.API;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author DELL
 */
@RestController
@AllArgsConstructor
public class UserController implements ApiRouteConstants {

    private final UserService userService;

    @GetMapping(API_USER_PROFILE)
    @ApiOperation("获取用户相关信息")
    public RestResponse<UserProfileDto> getProfile(@PathVariable("userId") long userId) {
        User user = userService.findUserById(userId);
        UserProfileDto dto = UserProfileDto.builder()
                .username(user.getUsername())
                .introduction(user.getIntroduction())
                .avatar(user.getAvatar())
                .likes(userService.getUserLikes(userId))
                .fans(userService.getUserFansCount(userId))
                .subscribers(userService.findSubscribeCount(Entity.USER, userId))
                .publish(userService.getUserPosts(userId))
                .gender(user.getGender())
                .build();
        return RestResponse.ok(dto);
    }

    @PostMapping(API_USER_PROFILE + API_USER_FOLLOW)
    @ApiOperation("关注用户")
    public RestResponse<Void> follow(@PathVariable("userId") long userId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userService.hasFollowed(principal.getId(), Entity.USER, userId)) {
            userService.unfollow(principal.getId(), Entity.USER, userId);
        } else {
            userService.follow(principal.getId(), Entity.USER, userId);
        }
        return RestResponse.ok();
    }

    @GetMapping(API_USER_PROFILE + API_USER_SUBSCRIBERS)
    @ApiOperation("获取自己的关注")
    public RestResponse<List<Map<String, Object>>> getSubscribers(@PathVariable("userId") long userId, @PathVariable("curPage") int curPage) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId().equals(userId)) {
            return RestResponse.ok(userService.findSubscribers(userId, curPage * 10, 10, Entity.USER));
        } else {
            throw new BusinessException(CommonResultStatus.FORBIDDEN, "只能查看自己的关注列表");
        }
    }

    @ApiOperation("获取自己的粉丝")
    @GetMapping(API_USER_PROFILE + API_USER_FANS)
    public RestResponse<List<Map<String, Object>>> getFans(@PathVariable("userId") long userId, @PathVariable("curPage") int curPage) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId().equals(userId)) {
            return RestResponse.ok(userService.findFans(userId, curPage * 10, 10));
        } else {
            throw new BusinessException(CommonResultStatus.FORBIDDEN, "只能查看自己的粉丝列表");
        }
    }

}
