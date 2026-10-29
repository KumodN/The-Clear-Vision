package com.example.theclearvision.feature.auth.service.impl;

import com.example.theclearvision.common.dto.response.AuthResponse;
import com.example.theclearvision.common.exception.BusinessException;
import com.example.theclearvision.common.dto.request.LoginRequest;
import com.example.theclearvision.common.dto.request.RegisterRequest;
import com.example.theclearvision.common.util.JwtUtil;
import com.example.theclearvision.domain.Address;
import com.example.theclearvision.domain.Role;
import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.auth.repository.AddressRepository;
import com.example.theclearvision.feature.auth.repository.RoleRepository;
import com.example.theclearvision.feature.auth.repository.UserRepository;
import com.example.theclearvision.feature.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("CUSTOMER");
                    newRole.setDescription("Regular customer");
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setRole(customerRole);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Create address
        Address address = new Address();
        address.setUser(savedUser);
        address.setLabel(registerRequest.getAddress().getLabel());
        address.setLine1(registerRequest.getAddress().getLine1());
        address.setLine2(registerRequest.getAddress().getLine2());
        address.setCity(registerRequest.getAddress().getCity());
        address.setPostalCode(registerRequest.getAddress().getPostalCode());
        address.setDefault(true);

        addressRepository.save(address);

        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get the user from database instead of casting from authentication
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new BusinessException("User not found after authentication"));

            String token = jwtUtil.generateToken(user);

            return AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().getName())
                    .build();

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new BusinessException("Invalid email or password");
        } catch (org.springframework.security.authentication.DisabledException e) {
            throw new BusinessException("Account is disabled");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Login failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()
                    || "anonymousUser".equals(authentication.getPrincipal())) {
                throw new BusinessException("User not authenticated");
            }

            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException("User not found"));

            return AuthResponse.builder()
                    .token(null) // Don't send token in profile response
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().getName())
                    .build();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Failed to get current user: " + e.getMessage());
        }
    }
}
