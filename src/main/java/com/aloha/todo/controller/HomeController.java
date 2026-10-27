package com.aloha.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 루트로 접속하면 API 문서(Swagger UI)로 이동
    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}
