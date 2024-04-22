package com.forum.mantoi.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.forum.mantoi.common.annotation.AccessInterceptor;
import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.pojo.dto.request.RegisterRequestDto;
import com.forum.mantoi.common.pojo.dto.response.RegisterResponseDto;
import com.forum.mantoi.common.pojo.vo.CaptchaVO;
import com.forum.mantoi.common.pojo.vo.SmsCaptchaVO;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @author DELL
 */
@RestController
@AllArgsConstructor
@Slf4j
@Tag(name = "登录验证接口")
public class AuthController implements ApiRouteConstants {

    private final UserServiceImpl userServiceImpl;

    private final StringRedisTemplate redisTemplate;

    @PostMapping(API_AUTH_PREFIX + API_REGISTER)
    @Operation(summary = "用户登录")
    public RestResponse<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return userServiceImpl.register(registerRequestDto);
    }

    @GetMapping(API_AUTH_PREFIX + API_CAPTCHA)
    @Operation(summary = "生成登录验证码")
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
    @Operation(summary = "短信验证码")
    @AccessInterceptor(key = "phone", permitsPerSecond = 0.1, blackListCount = 3, fallbackMethod = "rateLimitError")
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
        //发短信的Service不打算写了，因为比较麻烦
        return RestResponse.ok(captchaVO);
    }

    /**
     * 短信验证fallback函数
     *
     * @param phone   电话号码
     * @param request HttpRequest
     * @return RestResponse
     */
    @Operation(summary = "fallback函数")
    public RestResponse<Void> rateLimitError(@RequestParam String phone, HttpServletRequest request) {
        log.info("RateLimitError:{},Request:{}", phone, request.toString());
        return RestResponse.fail(CommonResultStatus.TOO_MANY_REQUEST);
    }


}
