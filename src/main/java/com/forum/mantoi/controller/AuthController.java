package com.forum.mantoi.controller;


import com.forum.mantoi.common.payload.RegisterRequest;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.UserRepository;
import com.forum.mantoi.sys.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest registerRequest, Model model) {
        Map<String, Object> map = userService.register(registerRequest);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功");
            model.addAttribute("next", "/login");
            //TODO:返回操作结果,以及完成operationHTML
            return "/operation";
        } else {
            model.addAttribute("usernameField", map.get("usernameField"));
            model.addAttribute("passwordField", map.get("passwordField"));
            model.addAttribute("emailField", map.get("emailField"));
            return "/register";
        }
    }

}
