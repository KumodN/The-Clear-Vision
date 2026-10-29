package com.example.theclearvision.feature.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatsDTO {
    private Long totalAppointments;
    private Long upcomingAppointments;
    private Long completedAppointments;
    private Long cancelledAppointments;
    private Map<String, Long> appointmentsByStatus;
    private List<BranchAppointments> appointmentsByBranch;
    private List<MonthlyAppointments> appointmentsByMonth;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchAppointments {
        private Long branchId;
        private String branchName;
        private Long count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyAppointments {
        private String month;
        private Long count;
    }
}
