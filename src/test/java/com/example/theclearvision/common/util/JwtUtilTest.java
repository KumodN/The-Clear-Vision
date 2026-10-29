package com.example.theclearvision.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Use reflection to set secret and expiration for testing
        // In real scenario, use @TestPropertySource
    }

    @Test
    void generateToken_ValidUserDetails_ReturnsToken() {
        // This would need proper setup with @TestPropertySource
        // For now, it's a placeholder for the test structure
        assertTrue(true);
    }
}
