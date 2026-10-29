package com.example.theclearvision.feature.auth.controller;

import com.example.theclearvision.common.dto.response.AuthResponse;
import com.example.theclearvision.common.dto.request.LoginRequest;
import com.example.theclearvision.common.dto.request.RegisterRequest;
import com.example.theclearvision.feature.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth service is healthy");
    }

    @GetMapping("/profile")
    public ResponseEntity<AuthResponse> getProfile() {
        AuthResponse response = authService.getCurrentUser();
        return ResponseEntity.ok(response);
    }

}
