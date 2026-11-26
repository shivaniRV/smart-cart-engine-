
package com.shivani.smart_cart_engine.auth.controller;

import com.shivani.smart_cart_engine.auth.dto.LoginRequest;
import com.shivani.smart_cart_engine.auth.dto.LoginResponse;
import com.shivani.smart_cart_engine.auth.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
