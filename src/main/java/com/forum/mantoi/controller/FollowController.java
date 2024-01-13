package com.forum.mantoi.controller;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.services.FollowService;
import com.forum.mantoi.sys.services.UserService;
import com.forum.mantoi.utils.CommunityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class FollowController {

    private final FollowService followService;

    private final UserService userService;

    private static final int PAGE_SIZE = 10;

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(Entity entity, long id) {
        User curUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        followService.follow(curUser.getId(), entity, id);
        //TODO:消息通知
        return CommunityUtil.getJsonString(CommonResultStatus.OK);
    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(Entity entity, long id) {
        User curUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        followService.unfollow(curUser.getId(), entity, id);
        return CommunityUtil.getJsonString(CommonResultStatus.OK);
    }

    @RequestMapping(path = "/subscribers/{userId}/{page}", method = RequestMethod.GET)
    public String getSubscribers(@PathVariable("userId") long userId, @PathVariable("page") int curPage, Model model) {
        User curUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User target = userService.findUserById(userId);
        model.addAttribute("target", target);
        model.addAttribute("curUser", curUser);
        int start = curPage * PAGE_SIZE;
        List<Map<String, Object>> subscribers = followService.findSubscribers(userId, start, PAGE_SIZE);
        if (subscribers != null) {
            for (Map<String, Object> map : subscribers) {
                User sub = (User) map.get("user");
                map.put("hasFollowed", followService.hasFollowed(curUser.getId(), Entity.USER, sub.getId()));
            }
        }
        model.addAttribute("subs", subscribers);
        return "/site/subscribers";
    }

    @RequestMapping(path = "/followers/{userId}/{page}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") long userId, @PathVariable("page") int curPage, Model model) {
        User curUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User target = userService.findUserById(userId);
        model.addAttribute("target", target);
        model.addAttribute("curUser", curUser);
        int start = curPage * PAGE_SIZE;
        List<Map<String, Object>> followers = followService.findFollowers(userId, start, PAGE_SIZE);
        if (followers != null) {
            for (Map<String, Object> map : followers) {
                User fo = (User) map.get("user");
                map.put("hasFollowed", followService.hasFollowed(curUser.getId(), Entity.USER, fo.getId()));
            }
        }
        model.addAttribute("follows", followers);
        return "/site/follower";
    }

}
