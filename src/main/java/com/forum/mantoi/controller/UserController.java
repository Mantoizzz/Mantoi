package com.forum.mantoi.controller;

import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.UserService;
import lombok.AllArgsConstructor;
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

    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") long userId, Model model) {
        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        long likeCount = likeService.viewLikes(Entity.USER, userId);
        model.addAttribute("likes", likeCount);
        int followers = user.getFollowers().size();
        model.addAttribute("followers", followers);
        int subscribers = user.getSubscribers().size();
        model.addAttribute("subscribers", subscribers);
        return null;
    }
}
