package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.payload.RegisterRequest;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    /**
     * 用户登录
     *
     * @param request RegisterRequest
     * @return Map
     */
    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> map = new HashMap<>();

        if (request == null) {
            throw new IllegalArgumentException("Null Parameter");
        }

        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            map.put("usernameField", "The name is used already!");
            return map;
        }

        user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            map.put("emailField", "This email is already used!");
            return map;
        }

        //开始注册
        User regisUser = new User();
        regisUser.setUsername(request.getUsername());
        regisUser.setPassword(passwordEncoder.encode(request.getPassword()));
        regisUser.setEmail(request.getEmail());
        regisUser.setCreateTime(new Date());
        userRepository.save(regisUser);
        return map;

    }

    //TODO:CaptchaController

}
