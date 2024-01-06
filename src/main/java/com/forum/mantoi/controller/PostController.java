package com.forum.mantoi.controller;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.common.payload.PostRequest;
import com.forum.mantoi.sys.entity.CommentPost;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.UserService;
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

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final LikeService likeService;

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
    public PostRequest publishPost() throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new UserException(CommonResultStatus.UNAUTHORIZED, "请登录再使用该功能");
        }
        return new PostRequest();
    }

    /*
    帖子列表
     */
    @ModelAttribute("postList")
    public Page<Post> pages(@PageableDefault(size = 10) Pageable pageable) {
        return postService.findAll(pageable);
    }

    @GetMapping
    public String post() {
        return "post";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute(name = "postRequest") PostRequest postRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new UserException(CommonResultStatus.UNAUTHORIZED, "请登录后再使用该功能");
        }
        postService.publish(user, postRequest);
        return CommunityUtil.getJsonString(CommonResultStatus.OK);
    }

//    @RequestMapping(path = "/detail/{postId}", method = RequestMethod.GET)
//    public String getPost(@PathVariable("postId") long postId, Model model, Pageable pageable) {
//
//        Post post = postService.findById(postId)
//                .orElseThrow(() -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "post does not exist"));
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User curUser = authentication == null ? null : (User) authentication.getPrincipal();
//
//        User author = post.getAuthor();
//        long likeCount = likeService.viewLikes(Entity.POST, postId);
//        boolean isLiked = curUser != null && likeService.isLiked(Entity.POST, postId, curUser.getId());
//        List<CommentPost> commentPosts = post.getCommentPosts();
//    }
}
