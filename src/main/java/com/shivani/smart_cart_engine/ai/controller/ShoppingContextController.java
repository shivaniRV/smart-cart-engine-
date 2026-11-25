package com.shivani.smart_cart_engine.ai.controller;

import com.shivani.smart_cart_engine.ai.dto.ShoppingContext;
import com.shivani.smart_cart_engine.ai.service.ShoppingContextService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class ShoppingContextController {

    private final ShoppingContextService shoppingContextService;

    public ShoppingContextController(
            ShoppingContextService shoppingContextService) {
        this.shoppingContextService = shoppingContextService;
    }

    @GetMapping("/context/{userId}")
    public ShoppingContext getShoppingContext(@PathVariable Long userId) {
        return shoppingContextService.buildShoppingContext(userId);
    }
}