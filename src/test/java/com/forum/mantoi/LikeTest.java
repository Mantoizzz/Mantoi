package com.forum.mantoi;

import com.forum.mantoi.common.constant.Entity;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LikeTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Test
    void like() {
        User user = getUser();
        likeService.addLike(user.getId(), getPost().getId(), Entity.POST);
    }

    User getUser() {
        return userService.findUserById(2L);
    }

    Post getPost() {
        return postService.getPost(1L);
    }

}
