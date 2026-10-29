package com.example.theclearvision.feature.admin.service.impl;

import com.example.theclearvision.feature.admin.dto.AppointmentStatsDTO;
import com.example.theclearvision.feature.admin.dto.CustomerStatsDTO;
import com.example.theclearvision.feature.admin.dto.ProductStatsDTO;
import com.example.theclearvision.feature.admin.dto.SalesStatsDTO;
import com.example.theclearvision.feature.admin.service.AdminStatsService;
import com.example.theclearvision.feature.appointment.AppointmentStatus;
import com.example.theclearvision.feature.appointment.repository.AppointmentRepository;
import com.example.theclearvision.feature.auth.repository.UserRepository;
import com.example.theclearvision.feature.order.domain.OrderEntity;
import com.example.theclearvision.feature.order.domain.OrderStatus;
import com.example.theclearvision.feature.order.repository.OrderRepository;
import com.example.theclearvision.persistence.ProductRepository;
import com.example.theclearvision.persistence.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStatsServiceImpl implements AdminStatsService {

        private final OrderRepository orderRepository;
        private final ProductRepository productRepository;
        private final ProductVariantRepository productVariantRepository;
        private final AppointmentRepository appointmentRepository;
        private final UserRepository userRepository;

        @Override
        public SalesStatsDTO getSalesStats() {
                // Get all paid orders
                var paidOrders = orderRepository.findAll().stream()
                                .filter(order -> order.getStatus() == OrderStatus.PAID
                                                || order.getStatus() == OrderStatus.DELIVERED)
                                .collect(Collectors.toList());

                // Calculate total revenue
                BigDecimal totalRevenue = paidOrders.stream()
                                .map(OrderEntity::getTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                Long totalOrders = (long) paidOrders.size();

                // Calculate average order value
                BigDecimal averageOrderValue = totalOrders > 0
                                ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                                : BigDecimal.ZERO;

                // Revenue by month (last 6 months)
                List<SalesStatsDTO.MonthlyRevenue> revenueByMonth = calculateMonthlyRevenue(paidOrders);

                // Revenue by status
                Map<String, BigDecimal> revenueByStatus = orderRepository.findAll().stream()
                                .collect(Collectors.groupingBy(
                                                order -> order.getStatus().name(),
                                                Collectors.reducing(BigDecimal.ZERO, OrderEntity::getTotal,
                                                                BigDecimal::add)));

                // Top selling products (placeholder - would need order items query)
                List<SalesStatsDTO.TopProduct> topProducts = new ArrayList<>();

                return SalesStatsDTO.builder()
                                .totalRevenue(totalRevenue)
                                .totalOrders(totalOrders)
                                .averageOrderValue(averageOrderValue)
                                .revenueByMonth(revenueByMonth)
                                .revenueByStatus(revenueByStatus)
                                .topSellingProducts(topProducts)
                                .build();
        }

        @Override
        public ProductStatsDTO getProductStats() {
                Long totalProducts = productRepository.count();
                Long totalVariants = productVariantRepository.count();
                Long activeProducts = productRepository.findAll().stream()
                                .filter(product -> Boolean.TRUE.equals(product.getIsActive()))
                                .count();

                // Low stock (< 10) and out of stock
                Long lowStockProducts = productVariantRepository.findAll().stream()
                                .filter(variant -> variant.getStockQuantity() > 0 && variant.getStockQuantity() < 10)
                                .count();

                Long outOfStockProducts = productVariantRepository.findAll().stream()
                                .filter(variant -> variant.getStockQuantity() == 0)
                                .count();

                // Products by category (placeholder)
                List<ProductStatsDTO.CategoryCount> productsByCategory = new ArrayList<>();

                // Average price - convert Double to BigDecimal
                BigDecimal totalPrice = productVariantRepository.findAll().stream()
                                .map(variant -> BigDecimal.valueOf(variant.getPrice()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal averagePrice = totalVariants > 0
                                ? totalPrice.divide(BigDecimal.valueOf(totalVariants), 2, RoundingMode.HALF_UP)
                                : BigDecimal.ZERO;

                return ProductStatsDTO.builder()
                                .totalProducts(totalProducts)
                                .totalVariants(totalVariants)
                                .activeProducts(activeProducts)
                                .lowStockProducts(lowStockProducts)
                                .outOfStockProducts(outOfStockProducts)
                                .productsByCategory(productsByCategory)
                                .averagePrice(averagePrice)
                                .build();
        }

        @Override
        public AppointmentStatsDTO getAppointmentStats() {
                Long totalAppointments = appointmentRepository.count();

                LocalDateTime now = LocalDateTime.now();
                Long upcomingAppointments = appointmentRepository.findAll().stream()
                                .filter(apt -> apt.getAppointmentTime().isAfter(now) &&
                                                apt.getStatus() == AppointmentStatus.SCHEDULED)
                                .count();

                Long completedAppointments = appointmentRepository.findAll().stream()
                                .filter(apt -> apt.getStatus() == AppointmentStatus.COMPLETED)
                                .count();

                Long cancelledAppointments = appointmentRepository.findAll().stream()
                                .filter(apt -> apt.getStatus() == AppointmentStatus.CANCELLED)
                                .count();

                // Appointments by status
                Map<String, Long> appointmentsByStatus = appointmentRepository.findAll().stream()
                                .collect(Collectors.groupingBy(
                                                apt -> apt.getStatus().name(),
                                                Collectors.counting()));

                // Appointments by branch (placeholder)
                List<AppointmentStatsDTO.BranchAppointments> appointmentsByBranch = new ArrayList<>();

                // Appointments by month (last 6 months)
                List<AppointmentStatsDTO.MonthlyAppointments> appointmentsByMonth = calculateMonthlyAppointments();

                return AppointmentStatsDTO.builder()
                                .totalAppointments(totalAppointments)
                                .upcomingAppointments(upcomingAppointments)
                                .completedAppointments(completedAppointments)
                                .cancelledAppointments(cancelledAppointments)
                                .appointmentsByStatus(appointmentsByStatus)
                                .appointmentsByBranch(appointmentsByBranch)
                                .appointmentsByMonth(appointmentsByMonth)
                                .build();
        }

        @Override
        public CustomerStatsDTO getCustomerStats() {
                Long totalCustomers = userRepository.count();

                // New customers this month
                LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0)
                                .withSecond(0);
                Long newCustomersThisMonth = userRepository.findAll().stream()
                                .filter(user -> user.getCreatedAt().isAfter(startOfMonth))
                                .count();

                // Customers with orders
                Long customersWithOrders = orderRepository.findAll().stream()
                                .map(order -> order.getUser().getId())
                                .distinct()
                                .count();

                // Active customers (with orders in last 3 months)
                LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
                Long activeCustomers = orderRepository.findAll().stream()
                                .filter(order -> order.getCreatedAt().isAfter(threeMonthsAgo))
                                .map(order -> order.getUser().getId())
                                .distinct()
                                .count();

                // Average orders per customer
                Long totalOrders = orderRepository.count();
                Double averageOrdersPerCustomer = customersWithOrders > 0
                                ? (double) totalOrders / customersWithOrders
                                : 0.0;

                // Top customers (placeholder)
                List<CustomerStatsDTO.TopCustomer> topCustomers = new ArrayList<>();

                return CustomerStatsDTO.builder()
                                .totalCustomers(totalCustomers)
                                .newCustomersThisMonth(newCustomersThisMonth)
                                .activeCustomers(activeCustomers)
                                .customersWithOrders(customersWithOrders)
                                .averageOrdersPerCustomer(averageOrdersPerCustomer)
                                .topCustomers(topCustomers)
                                .build();
        }

        // Helper methods
        private List<SalesStatsDTO.MonthlyRevenue> calculateMonthlyRevenue(List<OrderEntity> orders) {
                // Placeholder - would calculate revenue per month for last 6 months
                return new ArrayList<>();
        }

        private List<AppointmentStatsDTO.MonthlyAppointments> calculateMonthlyAppointments() {
                // Placeholder - would calculate appointments per month for last 6 months
                return new ArrayList<>();
        }
}
