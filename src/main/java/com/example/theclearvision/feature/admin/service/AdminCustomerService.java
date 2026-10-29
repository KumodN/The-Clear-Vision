package com.example.theclearvision.feature.admin.service;

import com.example.theclearvision.feature.auth.dto.UserDTO;
import java.util.List;

public interface AdminCustomerService {
    List<UserDTO> getCustomers(String search, String sortBy, String sortDir);

    UserDTO getCustomerDetail(Long id);

    UserDTO updateCustomer(Long id, UserDTO userDTO);

    void deleteCustomer(Long id);
}
