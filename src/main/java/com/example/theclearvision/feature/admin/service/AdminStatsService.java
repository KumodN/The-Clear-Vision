package com.example.theclearvision.feature.admin.service;

import com.example.theclearvision.feature.admin.dto.AppointmentStatsDTO;
import com.example.theclearvision.feature.admin.dto.CustomerStatsDTO;
import com.example.theclearvision.feature.admin.dto.ProductStatsDTO;
import com.example.theclearvision.feature.admin.dto.SalesStatsDTO;

public interface AdminStatsService {
    SalesStatsDTO getSalesStats();

    ProductStatsDTO getProductStats();

    AppointmentStatsDTO getAppointmentStats();

    CustomerStatsDTO getCustomerStats();
}
