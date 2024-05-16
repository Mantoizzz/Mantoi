package com.forum.mantoi;

import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.UserService;
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


}
