package com.forum.mantoi.controller;

import com.forum.mantoi.common.annotation.AccessInterceptor;
import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.common.pojo.dto.response.UserProfileDto;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author DELL
 */
@RestController
@AllArgsConstructor
@Tag(name = "用户相关api")
public class UserController implements ApiRouteConstants {

    private final UserService userService;

    @GetMapping(API_USER_PROFILE)
    @Operation(summary = "获取用户相关信息")
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
    @Operation(summary = "关注用户")
    @AccessInterceptor(key = "userId", permitsPerSecond = 1, blackListCount = 20, fallbackMethod = "followRateLimiterError")
    public RestResponse<Void> follow(@PathVariable("userId") long userId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userService.hasFollowed(principal.getId(), Entity.USER, userId)) {
            userService.unfollow(principal.getId(), Entity.USER, userId);
        } else {
            userService.follow(principal.getId(), Entity.USER, userId);
        }
        return RestResponse.ok();
    }

    @Operation(summary = "关注用户限流fallback函数")
    public RestResponse<Void> followRateLimiterError(@PathVariable("userId") long userId) {
        return RestResponse.fail(CommonResultStatus.TOO_MANY_REQUEST);
    }

    @GetMapping(API_USER_PROFILE + API_USER_SUBSCRIBERS)
    @Operation(summary = "获取自己的关注")
    public RestResponse<List<Map<String, Object>>> getSubscribers(@PathVariable("userId") long userId, @PathVariable("curPage") int curPage) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId().equals(userId)) {
            return RestResponse.ok(userService.findSubscribers(userId, curPage * 10, 10, Entity.USER));
        } else {
            throw new BusinessException(CommonResultStatus.FORBIDDEN, "只能查看自己的关注列表");
        }
    }

    @Operation(summary = "获取自己的粉丝")
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
