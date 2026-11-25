package com.shivani.smart_cart_engine.ai.dto;

import java.util.List;

public class ShoppingContext {

    private Long userId;
    private List<String> cartProducts;
    private List<String> previouslyPurchasedProducts;
    private List<ProductCandidate> availableProducts;

    public ShoppingContext() {
    }

    public ShoppingContext(Long userId,
                           List<String> cartProducts,
                           List<String> previouslyPurchasedProducts,
                           List<ProductCandidate> availableProducts) {
        this.userId = userId;
        this.cartProducts = cartProducts;
        this.previouslyPurchasedProducts = previouslyPurchasedProducts;
        this.availableProducts = availableProducts;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<String> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public List<String> getPreviouslyPurchasedProducts() {
        return previouslyPurchasedProducts;
    }

    public void setPreviouslyPurchasedProducts(List<String> previouslyPurchasedProducts) {
        this.previouslyPurchasedProducts = previouslyPurchasedProducts;
    }

    public List<ProductCandidate> getAvailableProducts() {
        return availableProducts;
    }

    public void setAvailableProducts(List<ProductCandidate> availableProducts) {
        this.availableProducts = availableProducts;
    }
}