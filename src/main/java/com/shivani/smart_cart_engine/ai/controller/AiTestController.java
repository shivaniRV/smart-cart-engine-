package com.shivani.smart_cart_engine.ai.controller;

import com.shivani.smart_cart_engine.ai.service.AiTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    private final AiTestService aiTestService;

    public AiTestController(AiTestService aiTestService) {
        this.aiTestService = aiTestService;
    }

    @GetMapping("/test")
    public String testAI(@RequestParam String question) {
        return aiTestService.askAI(question);
    }
}