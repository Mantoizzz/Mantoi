package com.forum.mantoi.controller;

import com.forum.mantoi.common.payload.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

    @GetMapping("/auth/register")
    public String register(Model model) {
        model.addAttribute("user", new RegisterRequest());
        return "register";
    }
}
