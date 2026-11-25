package com.shivani.smart_cart_engine.ai.dto;

public class ProductRecommendation {

    private Long productId;
    private String reason;

    public ProductRecommendation() {
    }

    public ProductRecommendation(Long productId, String reason) {
        this.productId = productId;
        this.reason = reason;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}