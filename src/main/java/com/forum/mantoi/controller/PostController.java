package com.forum.mantoi.controller;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.pojo.request.DeletePostDto;
import com.forum.mantoi.common.pojo.request.PublishCommentDto;
import com.forum.mantoi.common.pojo.response.PostDetailResponse;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.pojo.request.PublishPostDto;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.sys.services.*;
import com.forum.mantoi.sys.services.impl.LikeServiceImpl;
import com.forum.mantoi.utils.CommunityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    /*
    当前登录的用户
     */
    @ModelAttribute("curUser")
    public String user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            return "Anonymous User";
        }
    }

    /*
    在发布一篇帖子前要判断当前用户是否为Anonymous User,否则不提供发布帖子功能
     */
    @ModelAttribute("postRequest")
    public PublishPostDto publishPost() throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new UserException(CommonResultStatus.UNAUTHORIZED, "请登录再使用该功能");
        }
        return new PublishPostDto();
    }

    /*
    帖子列表
     */
    @ModelAttribute("postList")
    public Page<Post> pages(@PageableDefault(size = 10) Pageable pageable) {
        return postService.findAll(pageable);
    }

    @PostMapping(API_POST_PREFIX + API_ADD)
    public RestResponse<Void> addPost(@RequestBody PublishPostDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dto.setAuthor(user);
        return postService.publish(dto);
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
        User curUser = authentication == null ? null : ((User) authentication.getPrincipal());
        User author = postService.getAuthor(post);
        boolean isLiked = curUser != null && likeService.isLiked(Entity.POST, postId, curUser.getId());
        long likes = likeService.viewLikes(Entity.POST, postId);
        List<Comment> comments = commentService.findComments(post);
        int pageSize = 10;
        int start = Math.max(0, curPage * pageSize);
        int end = Math.min(start + pageSize, comments.size());
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
    public RestResponse<Void> delete(@PathVariable("postId") long postId) {
        return postService.delete(new DeletePostDto(postId));
    }


    @PostMapping(API_POST_PREFIX + API_POST_ADD_COMMENT)
    @ResponseBody
    public RestResponse<Void> addComment(@PathVariable("postId") long postId, @RequestBody PublishCommentDto dto) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    @ResponseBody
    public RestResponse<Void> addReply(@PathVariable long commentId, @RequestBody PublishCommentDto dto) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = Comment.builder()
                .authorId(principal.getId())
                .postId(null)
                .parentId(commentId)
                .content(dto.getContent())
                .likes(0)
                .build();
        return commentService.save(comment);
    }


}
