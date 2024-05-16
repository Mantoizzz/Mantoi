package com.forum.mantoi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.common.pojo.dto.request.DeletePostDto;
import com.forum.mantoi.common.pojo.dto.request.PostInformationDto;
import com.forum.mantoi.common.pojo.dto.request.PublishCommentDto;
import com.forum.mantoi.common.pojo.dto.request.PublishPostDto;
import com.forum.mantoi.common.pojo.dto.response.PostDetailResponse;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.model.SysUser;
import com.forum.mantoi.sys.services.CommentService;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

/**
 * @author DELL
 */
@AllArgsConstructor
@RestController
@Tag(name = "跟帖子相关的API")
public class PostController implements ApiRouteConstants {

    private final PostService postService;

    private final LikeService likeService;

    private final CommentService commentService;

    private final UserService userService;

    @PostMapping(API_POST_PREFIX + API_ADD)
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','VIP')")
    @Operation(summary = "添加Post")
    public RestResponse<Void> addPost(@RequestBody PublishPostDto dto) throws IOException {
        SysUser user = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dto.setAuthor(userService.convert(user));
        return postService.publish(dto);
    }


    @GetMapping(API_POST_PREFIX + API_POST_LOAD_MORE)
    @Operation(summary = "加载更多帖子")
    public RestResponse<Page<Post>> loadMore(@RequestParam(value = "page", defaultValue = "1") int page
            , @RequestParam(value = "size", defaultValue = "10") int size
            , HttpServletRequest request) {
        Page<Post> posts = postService.findPosts(size, page, request);
        return RestResponse.ok(posts);
    }

    /**
     * 1.先拿到PostInformationDTO
     * 2.
     *
     * @param postId id
     * @param page   page
     * @return RestResponse
     */
    @Operation(summary = "获取帖子信息api")
    @GetMapping(API_POST_PREFIX + API_POST_DETAIL)
    public RestResponse<PostDetailResponse> getPostDetail(@PathVariable(value = "postId") Long postId, @PathVariable int page) {

        PostInformationDto postDetail = postService.getPostDetail(postId);
        if (Objects.isNull(postDetail)) {
            return RestResponse.fail(null, CommonResultStatus.RECORD_NOT_EXIST);
        }
        SysUser principal = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean liked = likeService.isLiked(Entity.POST, postId, principal.getId());
        User author = postDetail.getAuthor();
        PostDetailResponse response = PostDetailResponse.builder()
                .post(postDetail.getPost())
                .curUser(principal)
                .author(author)
                .likes(postDetail.getPost().getLikes())
                .isLiked(liked)
                .commentVOList(postDetail.getCommentList())
                .build();
        return RestResponse.ok(response);
    }

    @DeleteMapping(API_POST_PREFIX + API_POST_DELETE)
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','VIP')")
    @Operation(summary = "删除帖子api")
    public RestResponse<Void> delete(@PathVariable("postId") long postId) {
        SysUser principal = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return postService.delete(new DeletePostDto(postId, principal.getId()));
    }

    @Operation(summary = "添加评论")
    @PostMapping(API_POST_PREFIX + API_POST_ADD_COMMENT)
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','VIP')")
    public RestResponse<Void> addComment(@PathVariable("postId") long postId, @RequestBody PublishCommentDto dto) {
        SysUser principal = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = Comment.builder()
                .postId(postId)
                .authorId(principal.getId())
                .parentId(null)
                .content(dto.getContent())
                .likes(0)
                .build();
        return commentService.save(comment);
    }

    @PostMapping(API_POST_PREFIX + API_POST_ADD_REPLY)
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','VIP')")
    @Operation(summary = "添加回复")
    public RestResponse<Void> addReply(@PathVariable long postId, @PathVariable long commentId, @RequestBody PublishCommentDto dto) {
        SysUser principal = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = Comment.builder()
                .authorId(principal.getId())
                .postId(postId)
                .parentId(commentId)
                .content(dto.getContent())
                .likes(0)
                .build();
        return commentService.save(comment);
    }

}
