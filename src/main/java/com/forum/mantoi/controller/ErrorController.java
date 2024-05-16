package com.forum.mantoi.controller;

import com.forum.mantoi.common.constant.ApiRouteConstants;
import com.forum.mantoi.common.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DELL
 */
@RestController
@Slf4j
public class ErrorController implements ApiRouteConstants {

    @GetMapping(API_ERROR)
    public RestResponse<Void> errorPage() {
        log.error("Something went wrong");
        return RestResponse.error();
    }

}
