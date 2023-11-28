package com.forum.mantoi;

import com.forum.mantoi.common.payload.CommentRequest;
import com.forum.mantoi.common.payload.PostRequest;
import com.forum.mantoi.sys.entity.Comment;
import com.forum.mantoi.sys.entity.CommentPost;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.model.Role;
import com.forum.mantoi.sys.repository.CommentPostRepository;
import com.forum.mantoi.sys.repository.CommentRepository;
import com.forum.mantoi.sys.repository.PostRepository;
import com.forum.mantoi.sys.repository.UserRepository;
import com.forum.mantoi.sys.services.CommentService;
import com.forum.mantoi.sys.services.PostService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
class MantoiApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentPostRepository commentPostRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Test
    void addUser() {
        User user = User.builder()
                .username("想飞天的牛奶蛋挞")
                .password(passwordEncoder.encode("david_steam1233"))
                .email("641538996@qq.com")
                .createTime(new Date())
                .followers(new ArrayList<>())
                .subscribers(new ArrayList<>())
                .posts(new ArrayList<>())
                .avatar(null)
                .role(Role.USER)
                .introduction("Hello World!")
                .build();
        userRepository.save(user);
    }

    @Test
    void addPost() {
        User user = userRepository.findByEmail("641538996@qq.com").get();
        PostRequest postRequest = new PostRequest();
        postRequest.setAuthor(user);
        postRequest.setContent("如题，最近hr面的时候，hr小姐姐说最近快要开奖了，如果到时候没有发意向就直接给我发offer。同公司同部门的其他同学都是意向了才等着开奖，想问一下佬们没有意向直接offer真的是可以的吗？还是有什么套路在里面呢");
        postRequest.setTitle("没有意向，直接offer ？？？");
        postRequest.setPublishTime(new Date());
        postService.publish(user, postRequest);
    }

    @Test
    void addComment() {
        User user = userRepository.findByEmail("641538994@qq.com").get();
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setCommentPost(commentPostRepository.findById(1L).get());
        commentRequest.setLikes(9);
        commentRequest.setPublishTime(new Date());
        commentRequest.setComment(new ArrayList<>());
        commentRequest.setContent("简直就是我");
        commentService.publish(user, commentRequest);
    }

    @Test
    void addCompost() {
        User user = userRepository.findByEmail("641538995@qq.com").get();
        CommentPost commentPost = new CommentPost();
        commentPost.setPost(postRepository.findPostById(2L).get());
        commentPost.setLikes(25);
        commentPost.setContent("校友，我也在boss上投了很多，情况跟你一样，大部分送达，少部分已读不回，感觉现在就没啥实习岗位啊");
        commentPost.setAuthor(user);
        commentPost.setPublishTime(new Date());
        commentPost.setComments(new ArrayList<>());
        commentPostRepository.save(commentPost);
    }

}
