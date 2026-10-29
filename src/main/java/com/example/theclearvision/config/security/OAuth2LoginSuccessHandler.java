package com.example.theclearvision.config.security;

import com.example.theclearvision.domain.AuthProvider;
import com.example.theclearvision.domain.Role;
import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.auth.repository.RoleRepository;
import com.example.theclearvision.feature.auth.repository.UserRepository;
import com.example.theclearvision.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        try {
            if (frontendUrl == null || frontendUrl.isEmpty()) {
                log.error("app.frontend.url is not configured!");
                throw new IllegalStateException("Frontend URL configuration is missing");
            }

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String providerId = oAuth2User.getAttribute("sub");

            log.info("OAuth2 login successful for email: {}", email);
            log.info("Using Frontend URL: '{}'", frontendUrl);

            // Find or create user
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> createNewUser(email, name, providerId));

            // Update provider info if user exists but was created locally
            if (user.getAuthProvider() == AuthProvider.LOCAL) {
                user.setAuthProvider(AuthProvider.GOOGLE);
                user.setProviderId(providerId);
                userRepository.save(user);
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user);

            // Clear authentication attributes
            clearAuthenticationAttributes(request);

            // Redirect to frontend with token
            String redirectUrl = String.format("%s/oauth2/redirect?token=%s", frontendUrl, token);
            log.info("Redirecting to: {}", redirectUrl);

            if (response.isCommitted()) {
                log.warn("Response already committed, cannot redirect to {}", redirectUrl);
                return;
            }

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } catch (Exception e) {
            log.error("Error during OAuth2 authentication", e);

            // Redirect to login page with error message
            String errorMessage = "Authentication failed: "
                    + (e.getMessage() != null ? e.getMessage() : "Unknown error");
            try {
                String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
                String errorUrl = String.format("%s/login?error=%s",
                        frontendUrl != null ? frontendUrl : "http://localhost:5173",
                        encodedError);

                if (!response.isCommitted()) {
                    getRedirectStrategy().sendRedirect(request, response, errorUrl);
                }
            } catch (Exception redirectEx) {
                log.error("Failed to redirect with error", redirectEx);
            }
        }
    }

    private User createNewUser(String email, String name, String providerId) {
        log.info("Creating new user from Google OAuth: {}", email);

        User user = new User();
        user.setEmail(email);

        // Parse name
        String[] nameParts = name != null ? name.split(" ", 2) : new String[] { "User", "" };
        user.setFirstName(nameParts[0]);
        user.setLastName(nameParts.length > 1 ? nameParts[1] : "");

        // Set OAuth fields
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setProviderId(providerId);

        // Set default values
        user.setPassword(""); // No password for OAuth users
        user.setPhone(""); // Can be updated later
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Assign default CUSTOMER role
        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(
                        () -> new RuntimeException("CUSTOMER role not found in database. Please run data seeder."));
        user.setRole(customerRole);

        return userRepository.save(user);
    }
}
