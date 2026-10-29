package com.example.theclearvision.feature.appointment.dto;

import com.example.theclearvision.feature.appointment.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long id;
    private Long userId;
    private Long branchId;
    private String branchName;
    private LocalDateTime appointmentTime;
    private Integer duration;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private String customerName;
    private String customerEmail;
}
