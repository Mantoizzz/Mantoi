package com.forum.mantoi;

import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.model.Role;
import com.forum.mantoi.sys.repository.UserRepository;
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
    private UserRepository userRepository;

    @Test
    void addUser() {
        User user = User.builder()
                .username("馒头龙")
                .password(passwordEncoder.encode("david_steam1233"))
                .email("641538994@qq.com")
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

}
