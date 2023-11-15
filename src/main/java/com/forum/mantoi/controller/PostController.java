package com.forum.mantoi.controller;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.common.payload.PostRequest;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

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




}
