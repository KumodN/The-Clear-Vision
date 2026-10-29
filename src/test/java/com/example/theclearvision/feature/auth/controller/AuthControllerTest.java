package com.example.theclearvision.feature.auth.controller;

import com.example.theclearvision.common.dto.request.AddressRequest;
import com.example.theclearvision.common.dto.request.RegisterRequest;
import com.example.theclearvision.domain.Role;
import com.example.theclearvision.feature.auth.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Pre-populate roles since Flyway is disabled in test
        if (roleRepository.findByName("CUSTOMER").isEmpty()) {
            Role role = new Role();
            role.setName("CUSTOMER");
            roleRepository.save(role);
        }
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("1234567890");

        AddressRequest address = new AddressRequest();
        address.setLabel("Home");
        address.setLine1("123 Test St");
        address.setCity("Test City");
        address.setPostalCode("12345");
        request.setAddress(address);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldFailRegistrationWithInvalidData() throws Exception {
        RegisterRequest request = new RegisterRequest();
        // Missing required fields

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
