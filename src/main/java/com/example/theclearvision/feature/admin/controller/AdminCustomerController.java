package com.example.theclearvision.feature.admin.controller;

import com.example.theclearvision.feature.admin.service.AdminCustomerService;
import com.example.theclearvision.feature.auth.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCustomerController {

    private final AdminCustomerService adminCustomerService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        List<UserDTO> customers = adminCustomerService.getCustomers(search, sortBy, sortDir);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getCustomer(@PathVariable Long id) {
        UserDTO customer = adminCustomerService.getCustomerDetail(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        UserDTO updatedCustomer = adminCustomerService.updateCustomer(id, userDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        adminCustomerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
