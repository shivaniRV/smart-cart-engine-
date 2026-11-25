package com.shivani.smart_cart_engine.ai.dto;

import java.util.List;

public class RecommendationResponse {

    private List<ProductRecommendation> recommendations;

    public RecommendationResponse() {
    }

    public RecommendationResponse(List<ProductRecommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public List<ProductRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<ProductRecommendation> recommendations) {
        this.recommendations = recommendations;
    }
}