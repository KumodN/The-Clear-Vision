package com.example.theclearvision.feature.doctor.service;

import com.example.theclearvision.feature.doctor.dto.CreateDoctorRequest;
import com.example.theclearvision.feature.doctor.dto.DoctorDTO;

import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors(Long branchId);

    DoctorDTO getDoctorById(Long id);

    DoctorDTO createDoctor(CreateDoctorRequest request);

    DoctorDTO updateDoctor(Long id, CreateDoctorRequest request);

    void deleteDoctor(Long id);
}
