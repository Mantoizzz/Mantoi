package com.forum.mantoi;

import com.forum.mantoi.common.pojo.dto.request.DeletePostDto;
import com.forum.mantoi.common.pojo.dto.request.PublishCommentDto;
import com.forum.mantoi.common.pojo.dto.request.PublishPostDto;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.services.CommentService;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;

@SpringBootTest
class DaoTest {

    @Autowired
    public UserService userService;

    @Autowired
    public PostService postService;

    @Autowired
    public CommentService commentService;

    @Test
    void addPost() throws IOException {
        PublishPostDto publishPostDto = new PublishPostDto();
        publishPostDto.setAuthor(getUser());
        publishPostDto.setTitle("许愿Offer");
        publishPostDto.setPublishTime(new Date());
        publishPostDto.setContent("现在正在找Java实习工作，求几个老哥一起模拟面试。\n" +
                "包括八股 ＋ 日常生活 ＋ hr等");
        publishPostDto.setShortContent("Java实习 模拟面试");
        postService.publish(publishPostDto);
        System.out.println("Successfully published post");
    }

    @Test
    void deletePost() {
        DeletePostDto deletePostDto = new DeletePostDto();
        deletePostDto.setPostId(2L);
        deletePostDto.setUserId(2L);
        postService.delete(deletePostDto);
    }

    @Test
    void addComment() {
        PublishCommentDto publishCommentDto = new PublishCommentDto();
        publishCommentDto.setAuthor(getUser());
        publishCommentDto.setContent("测试");
        publishCommentDto.setPostId(1L);
        publishCommentDto.setParentId(-1L);
        commentService.publish(publishCommentDto);
    }


     User getUser() {
        return userService.findUserById(2L);
    }

}
