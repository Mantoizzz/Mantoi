package com.forum.mantoi.controller;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.pojo.request.RegisterRequestDto;
import com.forum.mantoi.common.pojo.response.RegisterResponseDto;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.services.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author DELL
 */
@RestController
@AllArgsConstructor
public class AuthController implements ApiRouteConstants {

    private final UserServiceImpl userServiceImpl;

    @PostMapping(API_AUTH_PREFIX + API_REGISTER)
    @ResponseBody
    public RestResponse<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return userServiceImpl.register(registerRequestDto);
    }


}
