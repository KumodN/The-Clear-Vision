package com.example.theclearvision.feature.appointment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {

    @NotNull(message = "Branch ID is required")
    private Long branchId;

    private Long doctorId; // Optional: doctor selection

    @NotNull(message = "Appointment time is required")
    private LocalDateTime appointmentTime;

    private String notes;
}
