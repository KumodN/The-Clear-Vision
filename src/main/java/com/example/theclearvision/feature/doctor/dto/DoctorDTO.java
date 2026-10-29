package com.example.theclearvision.feature.doctor.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String qualifications;
    private String bio;
    private String photoUrl;
    private String email;
    private String phone;
    private Boolean isActive;
    private Long branchId;
    private String branchName;
    private LocalDateTime createdAt;
}
