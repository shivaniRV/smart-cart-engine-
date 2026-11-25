package com.shivani.smart_cart_engine.ai.dto;

public class ProductCandidate {

    private Long productId;
    private String name;
    private String category;
    private String description;

    public ProductCandidate() {
    }

    public ProductCandidate(Long productId, String name,
                            String category, String description) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}