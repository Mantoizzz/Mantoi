package com.forum.mantoi.controller;

import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.PostService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class HomeController {

    private final PostService postService;

    private final LikeService likeService;

    private final int PAGE_SIZE = 10;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String root() {
        return "forward:/index";
    }

    @RequestMapping(path = "/index/{page}", method = RequestMethod.GET)
    public String getIndexPage(@PathVariable("page") int curPage, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curUser = authentication == null ? null : (User) authentication.getPrincipal();
        model.addAttribute("user", curUser);
        List<Post> list = postService.findTopPosts();
        int start = Math.max(0, curPage * PAGE_SIZE);
        int end = Math.min(start + PAGE_SIZE, list.size());
        List<Map<String, Object>> vo = new ArrayList<>();
        list = list.subList(start, end);
        for (Post post : list) {
            Map<String, Object> map = new HashMap<>();
            User author = post.getAuthor();
            long likeCount = likeService.viewLikes(Entity.POST, post.getId());
            boolean isLiked = curUser != null && likeService.isLiked(Entity.POST, post.getId(), curUser.getId());
            int commentCount = post.getComments().size();
            map.put("author", author);
            map.put("likeCount", likeCount);
            map.put("isLiked", isLiked);
            map.put("commentCount", commentCount);
            vo.add(map);
        }
        model.addAttribute("posts", vo);
        return "/index/{page}";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }

    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
        return "/error/404";
    }

}
