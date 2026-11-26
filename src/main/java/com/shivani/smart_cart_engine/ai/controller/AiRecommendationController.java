
package com.shivani.smart_cart_engine.ai.controller;

import com.shivani.smart_cart_engine.ai.dto.RecommendationResponse;
import com.shivani.smart_cart_engine.ai.service.AiRecommendationService;
import com.shivani.smart_cart_engine.auth.service.AuthenticatedUserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;
    private final AuthenticatedUserService authenticatedUserService;

    public AiRecommendationController(
            AiRecommendationService aiRecommendationService,
            AuthenticatedUserService authenticatedUserService) {

        this.aiRecommendationService = aiRecommendationService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/recommendations")
    public RecommendationResponse getRecommendations() {

        Long userId = authenticatedUserService.getCurrentUserId();

        return aiRecommendationService.getRecommendations(userId);
    }
}
