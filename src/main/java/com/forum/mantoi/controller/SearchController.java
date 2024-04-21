package com.forum.mantoi.controller;

import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author DELL
 */
@RestController("search")
@AllArgsConstructor
@Tag(name = "搜索功能API")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("user")
    @Operation(summary = "搜索用户")
    public List<User> searchUser(String input) {
        return searchService.searchUser(input);
    }

    @GetMapping("post")
    @Operation(summary = "搜索帖子")
    public List<Post> searchPost(String input) throws IOException {
        return searchService.searchPost(input);
    }

}
