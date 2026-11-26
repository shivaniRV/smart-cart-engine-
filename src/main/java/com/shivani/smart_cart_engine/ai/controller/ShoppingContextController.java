
package com.shivani.smart_cart_engine.ai.controller;

import com.shivani.smart_cart_engine.ai.dto.ShoppingContext;
import com.shivani.smart_cart_engine.ai.service.ShoppingContextService;
import com.shivani.smart_cart_engine.auth.service.AuthenticatedUserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class ShoppingContextController {

    private final ShoppingContextService shoppingContextService;
    private final AuthenticatedUserService authenticatedUserService;

    public ShoppingContextController(
            ShoppingContextService shoppingContextService,
            AuthenticatedUserService authenticatedUserService) {

        this.shoppingContextService = shoppingContextService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/context")
    public ShoppingContext getShoppingContext() {

        Long userId = authenticatedUserService.getCurrentUserId();

        return shoppingContextService.buildShoppingContext(userId);
    }
}
