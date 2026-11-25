package com.shivani.smart_cart_engine.ai.dto;

public class RecommendationRequest {

    private Long userId;

    public RecommendationRequest() {
    }

    public RecommendationRequest(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}