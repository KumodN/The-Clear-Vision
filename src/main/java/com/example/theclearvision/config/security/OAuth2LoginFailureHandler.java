package com.example.theclearvision.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        log.error("OAuth2 Login Failed: {}", exception.getMessage(), exception);

        String targetUrl = frontendUrl != null ? frontendUrl : "http://localhost:5173";
        String errorMessage = "Authentication failed: " + exception.getMessage();

        try {
            String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
            targetUrl += "/login?error=" + encodedError;
        } catch (Exception e) {
            log.error("Error encoding error message", e);
            targetUrl += "/login?error=authentication_failed";
        }

        log.info("Redirecting to failure URL: {}", targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
