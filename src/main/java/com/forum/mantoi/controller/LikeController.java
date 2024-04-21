package com.forum.mantoi.controller;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.services.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author DELL
 */
@RestController
@AllArgsConstructor
@Tag(name = "点赞接口")
public class LikeController implements ApiRouteConstants {

    private final LikeService likeService;

    @PostMapping(API_LIKE)
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','VIP')")
    @Operation(summary = "点赞接口")
    public RestResponse<Void> like(Entity entity, long id, long userId) {
        likeService.addLike(userId, id, entity);
        return RestResponse.ok();
    }
}
