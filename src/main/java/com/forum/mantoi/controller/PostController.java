package com.forum.mantoi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.pojo.dto.request.DeletePostDto;
import com.forum.mantoi.common.pojo.dto.request.PublishCommentDto;
import com.forum.mantoi.common.pojo.dto.response.PostDetailResponse;
import com.forum.mantoi.common.pojo.dto.request.PublishPostDto;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.sys.model.SysUser;
import com.forum.mantoi.sys.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * @author DELL
 */
@AllArgsConstructor
@RestController
public class PostController implements ApiRouteConstants {

    private final PostService postService;

    private final LikeService likeService;

    private final CommentService commentService;

    private final UserService userService;

    private final SearchService searchService;

    @PostMapping(API_POST_PREFIX + API_ADD)
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','VIP')")
    public RestResponse<Void> addPost(@RequestBody PublishPostDto dto) throws IOException {
        SysUser user = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dto.setAuthor(userService.convert(user));
        return postService.publish(dto);
    }


    @GetMapping(API_POST_PREFIX + API_POST_LOAD_MORE)
    public RestResponse<Page<Post>> loadMore(@RequestParam(value = "page", defaultValue = "1") int page
            , @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Post> posts = postService.findPosts(size, page);
        return RestResponse.ok(posts);
    }


    /**
     * 访问论坛的处理方法
     *
     * @param postId  postId
     * @param curPage 目前的page数
     * @return URL
     */
    @GetMapping(API_POST_PREFIX + API_POST_DETAIL)
    public RestResponse<PostDetailResponse> getPostDetail(@PathVariable("postId") long postId, @PathVariable("page") int curPage) {
        Post post = postService.findById(postId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SysUser curUser = authentication == null ? null : ((SysUser) authentication.getPrincipal());
        User author = postService.getAuthor(post);
        boolean isLiked = curUser != null && likeService.isLiked(Entity.POST, postId, curUser.getId());
        long likes = likeService.viewLikes(Entity.POST, postId);
        List<Comment> comments = commentService.findComments(post);
        List<Map<String, Object>> commentVOList = new ArrayList<>();
        if (!comments.isEmpty()) {
            for (Comment comment : comments) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment);
                map.put("author", userService.findUserById(comment.getAuthorId()));
                map.put("likes", comment.getLikes());
                boolean commentLiked = curUser != null && likeService.isLiked(Entity.COMMENT, comment.getId(), curUser.getId());
                map.put("isLiked", commentLiked);
                List<Comment> replyList = commentService.findReply(comment);
                List<Map<String, Object>> replyVOList = new ArrayList<>();
                if (!replyList.isEmpty()) {
                    for (Comment reply : replyList) {
                        Map<String, Object> vo = new HashMap<>();
                        vo.put("reply", reply);
                        vo.put("replyAuthor", userService.findUserById(reply.getAuthorId()));
                        long likeCnt = likeService.viewLikes(Entity.COMMENT, reply.getId());
                        vo.put("likeCnt", likeCnt);
                        boolean likeStatus = curUser != null && likeService.isLiked(Entity.COMMENT, reply.getId(), curUser.getId());
                        vo.put("likeStatus", likeStatus);
                        replyVOList.add(vo);
                    }
                }
                map.put("reply", replyVOList);
                int commentCount = comments.size();
                map.put("commentCount", commentCount);
                commentVOList.add(map);
            }
        }
        PostDetailResponse response = PostDetailResponse.builder()
                .post(post)
                .likes(likes)
                .author(author)
                .comments(commentVOList)
                .curUser(curUser)
                .isLiked(isLiked)
                .build();
        return RestResponse.ok(response);
    }

    @DeleteMapping(API_POST_PREFIX + API_POST_DELETE)
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','VIP')")
    public RestResponse<Void> delete(@PathVariable("postId") long postId) {
        SysUser principal = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return postService.delete(new DeletePostDto(postId, principal.getId()));
    }


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
    public RestResponse<Void> addReply(@PathVariable long commentId, @RequestBody PublishCommentDto dto) {
        SysUser principal = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = Comment.builder()
                .authorId(principal.getId())
                .postId(null)
                .parentId(commentId)
                .content(dto.getContent())
                .likes(0)
                .build();
        return commentService.save(comment);
    }

    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','VIP')")
    public RestResponse<List<Post>> searchPost(String input) throws IOException {
        List<?> search = searchService.search(input, Post.class);
        List<Post> res = new ArrayList<>();
        for (var obj : search) {
            res.add((Post) obj);
        }
        return RestResponse.ok(res);
    }

}
