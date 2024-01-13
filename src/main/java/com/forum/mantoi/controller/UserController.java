package com.forum.mantoi.controller;

import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.services.FollowService;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final LikeService likeService;

    private final FollowService followService;


    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") long userId, Model model) {
        User curUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        long likeCount = likeService.viewLikes(Entity.USER, userId);
        model.addAttribute("likes", likeCount);
        long followers = followService.findFollowerCount(Entity.USER, userId);
        model.addAttribute("followers", followers);
        long subscribers = followService.findSubscribersCount(Entity.USER, userId);
        model.addAttribute("subscribers", subscribers);
        boolean hasFollowed = followService.hasFollowed(curUser.getId(), Entity.USER, userId);
        model.addAttribute("hasFollowed", hasFollowed);
        return "/site/profile";
    }
}
