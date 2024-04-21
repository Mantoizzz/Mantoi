package com.forum.mantoi.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.pojo.dto.request.RegisterRequestDto;
import com.forum.mantoi.common.pojo.dto.response.RegisterResponseDto;
import com.forum.mantoi.common.pojo.vo.CaptchaVO;
import com.forum.mantoi.common.pojo.vo.SmsCaptchaVO;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.services.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @author DELL
 */
@RestController
@AllArgsConstructor
@Api(tags = "登录验证接口")
public class AuthController implements ApiRouteConstants {

    private final UserServiceImpl userServiceImpl;

    private final StringRedisTemplate redisTemplate;

    @PostMapping(API_AUTH_PREFIX + API_REGISTER)
    @ApiOperation(value = "用户登录")
    public RestResponse<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return userServiceImpl.register(registerRequestDto);
    }

    @GetMapping(API_AUTH_PREFIX + API_CAPTCHA)
    @ApiOperation(value = "生成登录验证码")
    public RestResponse<CaptchaVO> generateCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100);
        String code = captcha.getCode();
        String base64 = captcha.getImageBase64();

        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setBase64(base64);
        captchaVO.setId(UUID.randomUUID().toString());

        redisTemplate.opsForValue().set(captchaVO.getId(), code, 3, TimeUnit.MINUTES);

        return RestResponse.ok(captchaVO);
    }

    @PostMapping(API_AUTH_PREFIX + API_SMS)
    @ApiOperation(value = "短信验证码")
    public RestResponse<SmsCaptchaVO> smsCaptcha(@RequestParam String phone, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(remoteAddr))) {
            RestResponse.fail(null, CommonResultStatus.TOO_MANY_REQUEST);
        }
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
        SmsCaptchaVO captchaVO = new SmsCaptchaVO();
        captchaVO.setPhone(phone);
        captchaVO.setExpire(5);
        redisTemplate.opsForValue().set(remoteAddr, RandomUtil.randomString(3), 1, TimeUnit.MINUTES);
        //TODO 发短信的Service
        return RestResponse.ok(captchaVO);
    }


}
