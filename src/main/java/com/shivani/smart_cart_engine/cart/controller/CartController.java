
package com.shivani.smart_cart_engine.cart.controller;

import com.shivani.smart_cart_engine.auth.service.AuthenticatedUserService;
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
    private final AuthenticatedUserService authenticatedUserService;

    public CartController(
            CartService cartService,
            AuthenticatedUserService authenticatedUserService) {

        this.cartService = cartService;
        this.authenticatedUserService = authenticatedUserService;
    }

    // Get logged-in user's cart
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {

        Long userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                cartService.getCart(userId));
    }

    // Add product to cart
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request) {

        Long userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                cartService.addToCart(userId, request));
    }

    // Update product quantity
    @PutMapping("/items")
    public ResponseEntity<CartResponse> updateCartItem(
            @Valid @RequestBody AddToCartRequest request) {

        Long userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                cartService.updateCartItem(userId, request));
    }

    // Remove a product from cart
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeCartItem(
            @PathVariable Long productId) {

        Long userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                cartService.removeCartItem(userId, productId));
    }

    // Clear entire cart
    @DeleteMapping("/items")
    public ResponseEntity<CartResponse> clearCart() {

        Long userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                cartService.clearCart(userId));
    }
}
