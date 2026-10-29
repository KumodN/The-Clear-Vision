package com.example.theclearvision.feature.doctor.service.impl;

import com.example.theclearvision.common.exception.ResourceNotFoundException;
import com.example.theclearvision.domain.Doctor;
import com.example.theclearvision.feature.branch.Branch;
import com.example.theclearvision.feature.branch.repository.BranchRepository;
import com.example.theclearvision.feature.doctor.dto.CreateDoctorRequest;
import com.example.theclearvision.feature.doctor.dto.DoctorDTO;
import com.example.theclearvision.feature.doctor.repository.DoctorRepository;
import com.example.theclearvision.feature.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDTO> getAllDoctors(Long branchId) {
        List<Doctor> doctors = branchId != null
                ? doctorRepository.findByBranchIdAndIsActiveTrue(branchId)
                : doctorRepository.findByIsActiveTrue();

        return doctors.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return toDTO(doctor);
    }

    @Override
    @Transactional
    public DoctorDTO createDoctor(CreateDoctorRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));

        Doctor doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .specialization(request.getSpecialization())
                .qualifications(request.getQualifications())
                .bio(request.getBio())
                .photoUrl(request.getPhotoUrl())
                .email(request.getEmail())
                .phone(request.getPhone())
                .isActive(request.getIsActive())
                .branch(branch)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);
        return toDTO(savedDoctor);
    }

    @Override
    @Transactional
    public DoctorDTO updateDoctor(Long id, CreateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));

        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualifications(request.getQualifications());
        doctor.setBio(request.getBio());
        doctor.setPhotoUrl(request.getPhotoUrl());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setIsActive(request.getIsActive());
        doctor.setBranch(branch);

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return toDTO(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    private DoctorDTO toDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setQualifications(doctor.getQualifications());
        dto.setBio(doctor.getBio());
        dto.setPhotoUrl(doctor.getPhotoUrl());
        dto.setEmail(doctor.getEmail());
        dto.setPhone(doctor.getPhone());
        dto.setIsActive(doctor.getIsActive());
        dto.setCreatedAt(doctor.getCreatedAt());

        if (doctor.getBranch() != null) {
            dto.setBranchId(doctor.getBranch().getId());
            dto.setBranchName(doctor.getBranch().getName());
        }

        return dto;
    }
}
