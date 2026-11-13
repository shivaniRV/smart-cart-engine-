package com.shivani.smart_cart_engine.cart.controller;

import com.shivani.smart_cart_engine.cart.dto.AddToCartRequest;
import com.shivani.smart_cart_engine.cart.dto.CartResponse;
import com.shivani.smart_cart_engine.cart.service.CartService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Get user's cart
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.getCart(userId));
    }

    // Add product to cart
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long userId,
            @Valid @RequestBody AddToCartRequest request) {

        return ResponseEntity.ok(
                cartService.addToCart(userId, request));
    }

    // Update product quantity
    @PutMapping("/{userId}/items")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long userId,
            @Valid @RequestBody AddToCartRequest request) {

        return ResponseEntity.ok(
                cartService.updateCartItem(userId, request));
    }

    // Remove a product from cart
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponse> removeCartItem(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                cartService.removeCartItem(userId, productId));
    }

    // Clear entire cart
    @DeleteMapping("/{userId}/items")
    public ResponseEntity<CartResponse> clearCart(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.clearCart(userId));
    }
}