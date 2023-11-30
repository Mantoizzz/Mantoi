package com.forum.mantoi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

    @GetMapping("/homePage")
    public String homePage() {
        return "homePage";
    }
}
