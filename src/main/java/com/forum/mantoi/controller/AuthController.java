package com.forum.mantoi.controller;


import com.forum.mantoi.common.payload.RegisterRequest;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest registerRequest) {
        User registerUser = new User();
        registerUser.setUsername(registerRequest.getUsername());
        registerUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        registerUser.setEmail(registerRequest.getEmail());
        registerUser.setRole("ROLE_USER");
        return userRepository.save(registerUser).toString();
    }

}
