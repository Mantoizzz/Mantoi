package com.forum.mantoi;

import com.forum.mantoi.common.pojo.dto.request.LoginRequestDto;
import com.forum.mantoi.common.pojo.dto.request.RegisterRequestDto;
import com.forum.mantoi.sys.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class AuthTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;


    @Test
    void login() {
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setUsername("Music");
        requestDto.setPassword("123456");
        HttpEntity<LoginRequestDto> httpEntity = new HttpEntity<>(requestDto);

        ResponseEntity<String> exchange = restTemplate.exchange("localhost:8080/auth/login", HttpMethod.POST, httpEntity, String.class);
    }

    @Test
    void addUser() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setUsername("Music");
        registerRequestDto.setPassword("123456");
        registerRequestDto.setEmail("Music@qq.com");
        registerRequestDto.setPhone("1234567891");
        userService.register(registerRequestDto);
        System.out.println("Successfully registered user");
    }
}
