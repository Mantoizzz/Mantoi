package com.forum.mantoi.controller;

import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.services.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "搜索功能API")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("user")
    @ApiOperation("搜索用户")
    public List<User> searchUser(String input) {
        return searchService.searchUser(input);
    }

    @GetMapping("post")
    @ApiOperation("搜索帖子")
    public List<Post> searchPost(String input) throws IOException {
        return searchService.searchPost(input);
    }

}
