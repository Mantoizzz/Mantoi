package com.forum.mantoi.controller;

import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @ModelAttribute("posts")
    public Page<Post> pages(@PageableDefault(size = 10) Pageable pageable) {
        return postService.findAll(pageable);
    }

}
