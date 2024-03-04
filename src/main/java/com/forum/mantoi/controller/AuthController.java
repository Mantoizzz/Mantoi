package com.forum.mantoi.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.pojo.dto.request.RegisterRequestDto;
import com.forum.mantoi.common.pojo.dto.response.RegisterResponseDto;
import com.forum.mantoi.common.pojo.vo.CaptchaVO;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.services.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


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


    @GetMapping(API_AUTH_PREFIX + API_CAPTCHA)
    @ResponseBody
    public RestResponse<CaptchaVO> generateCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100);
        String code = captcha.getCode();

    }


}
