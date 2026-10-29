package com.example.theclearvision.feature.doctor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDoctorRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String specialization;
    private String qualifications;
    private String bio;
    private String photoUrl;
    private String email;
    private String phone;

    @NotNull(message = "Branch ID is required")
    private Long branchId;

    private Boolean isActive = true;
}
