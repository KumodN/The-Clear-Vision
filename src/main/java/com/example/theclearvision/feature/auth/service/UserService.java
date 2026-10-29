package com.example.theclearvision.feature.auth.service;

import com.example.theclearvision.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getUserByEmail(String email);
    boolean userExists(String email);
}
