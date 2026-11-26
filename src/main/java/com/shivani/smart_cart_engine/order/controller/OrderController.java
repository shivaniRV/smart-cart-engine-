
package com.shivani.smart_cart_engine.order.controller;

import com.shivani.smart_cart_engine.auth.service.AuthenticatedUserService;
import com.shivani.smart_cart_engine.order.dto.OrderResponse;
import com.shivani.smart_cart_engine.order.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthenticatedUserService authenticatedUserService;

    public OrderController(
            OrderService orderService,
            AuthenticatedUserService authenticatedUserService) {

        this.orderService = orderService;
        this.authenticatedUserService = authenticatedUserService;
    }

    // Checkout and place order
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout() {

        Long userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                orderService.checkout(userId));
    }

    // Get logged-in user's order history
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrderHistory() {

        Long userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                orderService.getOrderHistory(userId));
    }

    // Get a specific order belonging to logged-in user
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId) {

        Long userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                orderService.getOrderById(userId, orderId));
    }
}
