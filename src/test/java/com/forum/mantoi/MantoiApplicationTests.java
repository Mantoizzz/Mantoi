package com.forum.mantoi;

import com.forum.mantoi.common.payload.PostRequest;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.model.Role;
import com.forum.mantoi.sys.repository.CommentRepository;
import com.forum.mantoi.sys.repository.PostRepository;
import com.forum.mantoi.sys.repository.UserRepository;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
class MantoiApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

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
    void test4Authentication() {
        String email = "641538994@qq.com";
    }

    @Test
    void test4Redis() {
        redisTemplate.opsForValue().set("hall", "frums");
    }

    @Test
    void test4Sub() {
        User user = userRepository.findByUsername("馒头龙").get();
        User target = userRepository.findByUsername("馒头蟹没头绪").get();
        userService.unSubscribe(user, target);
    }

    @Test
    void test4unSub() {
        User user = userRepository.findByUsername("馒头龙").get();
        User target = userRepository.findByUsername("馒头蟹没头绪").get();
        userService.unSubscribe(user, target);
    }

    @Test
    void test() {
        System.out.println("Testing");
    }

}
