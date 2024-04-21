package com.forum.mantoi.controller;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.impl.LikeServiceImpl;
import com.forum.mantoi.utils.CommunityUtil;
import com.forum.mantoi.utils.RedisKeys;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DELL
 */
@RestController
@AllArgsConstructor
@Api(tags = "点赞接口")
public class LikeController implements ApiRouteConstants {

    private final LikeService likeService;

    @PostMapping(API_LIKE)
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','VIP')")
    @ApiOperation(value = "点赞接口")
    public RestResponse<Void> like(Entity entity, long id, long userId) {
        likeService.addLike(userId, id, entity);
        return RestResponse.ok();
    }
}
