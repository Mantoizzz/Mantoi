package com.forum.mantoi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

    @GetMapping("/post")
    public String post() {
        return "post";
    }
}
