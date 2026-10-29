package com.example.theclearvision.feature.auth.service;

import com.example.theclearvision.common.dto.response.AuthResponse;
import com.example.theclearvision.common.dto.request.LoginRequest;
import com.example.theclearvision.common.dto.request.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse getCurrentUser();
}
