package com.example.theclearvision.feature.admin.controller;

import com.example.theclearvision.feature.admin.dto.AppointmentStatsDTO;
import com.example.theclearvision.feature.admin.dto.CustomerStatsDTO;
import com.example.theclearvision.feature.admin.dto.ProductStatsDTO;
import com.example.theclearvision.feature.admin.dto.SalesStatsDTO;
import com.example.theclearvision.feature.admin.service.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @GetMapping("/sales")
    public ResponseEntity<SalesStatsDTO> getSalesStats() {
        SalesStatsDTO stats = adminStatsService.getSalesStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/products")
    public ResponseEntity<ProductStatsDTO> getProductStats() {
        ProductStatsDTO stats = adminStatsService.getProductStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/appointments")
    public ResponseEntity<AppointmentStatsDTO> getAppointmentStats() {
        AppointmentStatsDTO stats = adminStatsService.getAppointmentStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/customers")
    public ResponseEntity<CustomerStatsDTO> getCustomerStats() {
        CustomerStatsDTO stats = adminStatsService.getCustomerStats();
        return ResponseEntity.ok(stats);
    }
}
