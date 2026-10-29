package com.example.theclearvision.feature.admin.service.impl;

import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.admin.service.AdminCustomerService;
import com.example.theclearvision.feature.auth.dto.UserDTO;
import com.example.theclearvision.feature.auth.repository.UserRepository;
import com.example.theclearvision.feature.auth.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCustomerServiceImpl implements AdminCustomerService {

    private final UserRepository userRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<UserDTO> getCustomers(String search, String sortBy, String sortDir) {
        Sort sort = Sort.unsorted();
        if (StringUtils.hasText(sortBy)) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
            String property = "createdAt"; // default
            if ("name".equalsIgnoreCase(sortBy)) {
                property = "firstName";
            } else if ("email".equalsIgnoreCase(sortBy)) {
                property = "email";
            }
            sort = Sort.by(direction, property);
        } else {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Specification<User> spec = UserSpecification.getUsers(search, null); // Show ALL users (including admin)

        return userRepository.findAll(spec, sort).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UserDTO getCustomerDetail(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDTO(user);
    }

    @Override
    public UserDTO updateCustomer(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhoneNumber());
        user.setIsActive(userDTO.getIsActive());

        user = userRepository.save(user);
        return toDTO(user);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhone());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        if (user.getRole() != null) {
            dto.setRole(user.getRole().getName());
        }
        return dto;
    }
}
