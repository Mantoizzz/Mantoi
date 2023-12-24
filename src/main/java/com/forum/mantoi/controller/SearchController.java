package com.forum.mantoi.controller;

import com.forum.mantoi.sys.elasticsearch.EsPost;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.model.Page;
import com.forum.mantoi.sys.services.ElasticsearchService;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class SearchController {

    private final ElasticsearchService service;

    private final UserService userService;

    private final LikeService likeService;

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) {
        org.springframework.data.domain.Page<EsPost> results = service.searchPost(keyword, page.getCurrent() - 1, page.getLimit());

        List<Map<String, Object>> posts = new ArrayList<>();
        if (results != null) {
            for (EsPost post : results) {
                Map<String, Object> map = new HashMap<>();

                map.put("post", posts);
                map.put("author", userService.findUserById(post.getAuthorId()));
                map.put("like", likeService.viewLikes(Entity.POST, post.getId()));

                posts.add(map);
            }
        }

        model.addAttribute("post", posts);
        model.addAttribute("keyword", keyword);

        page.setPath("search?keyword=" + keyword);
        page.setRows(results == null ? 0 : (int) results.getTotalElements());

        return "/search";

    }
}
