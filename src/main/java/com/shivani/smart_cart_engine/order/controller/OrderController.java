package com.shivani.smart_cart_engine.order.controller;

import com.shivani.smart_cart_engine.order.dto.OrderResponse;
import com.shivani.smart_cart_engine.order.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Checkout and place order
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                orderService.checkout(userId));
    }

    // Get all orders of a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrderHistory(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                orderService.getOrderHistory(userId));
    }

    // Get a specific order
    @GetMapping("/{userId}/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long userId,
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(userId, orderId));
    }
}