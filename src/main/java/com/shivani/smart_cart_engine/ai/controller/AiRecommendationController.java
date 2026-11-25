package com.shivani.smart_cart_engine.ai.controller;


import com.shivani.smart_cart_engine.ai.service.AiRecommendationService;
import org.springframework.web.bind.annotation.*;
import com.shivani.smart_cart_engine.ai.dto.RecommendationResponse;
@RestController
@RequestMapping("/api/ai")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    public AiRecommendationController(
            AiRecommendationService aiRecommendationService) {
        this.aiRecommendationService = aiRecommendationService;
    }

    @GetMapping("/recommendations/{userId}")
    public RecommendationResponse getRecommendations(
            @PathVariable Long userId) {

        return aiRecommendationService.getRecommendations(userId);
    }
}