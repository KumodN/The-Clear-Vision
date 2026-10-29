package com.example.theclearvision.feature.admin.service.impl;

import com.example.theclearvision.feature.admin.service.AdminAppointmentService;
import com.example.theclearvision.feature.appointment.Appointment;
import com.example.theclearvision.feature.appointment.AppointmentStatus;
import com.example.theclearvision.feature.appointment.dto.AppointmentDTO;
import com.example.theclearvision.feature.appointment.repository.AppointmentRepository;
import com.example.theclearvision.feature.appointment.repository.AppointmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAppointmentServiceImpl implements AdminAppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointments(String search, String status, String startDate, String endDate,
            String sortBy, String sortDir) {
        Sort sort = Sort.unsorted();
        if (StringUtils.hasText(sortBy)) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
            String property = "appointmentTime"; // default
            if ("status".equalsIgnoreCase(sortBy)) {
                property = "status";
            } else if ("createdAt".equalsIgnoreCase(sortBy)) {
                property = "createdAt";
            }
            sort = Sort.by(direction, property);
        } else {
            sort = Sort.by(Sort.Direction.DESC, "appointmentTime");
        }

        LocalDateTime start = null;
        LocalDateTime end = null;
        if (StringUtils.hasText(startDate)) {
            start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME);
        }
        if (StringUtils.hasText(endDate)) {
            end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME);
        }

        Specification<Appointment> spec = AppointmentSpecification.getAppointments(search, status, start, end);

        return appointmentRepository.findAll(spec, sort).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentDetail(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return toDTO(appointment);
    }

    @Override
    public AppointmentDTO updateAppointmentStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        try {
            appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
            appointment = appointmentRepository.save(appointment);
            return toDTO(appointment);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found");
        }
        appointmentRepository.deleteById(id);
    }

    private AppointmentDTO toDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setDuration(appointment.getDurationMinutes());
        dto.setStatus(appointment.getStatus());
        dto.setNotes(appointment.getNotes());
        dto.setCreatedAt(appointment.getCreatedAt());

        if (appointment.getUser() != null) {
            dto.setUserId(appointment.getUser().getId());
            dto.setCustomerName(appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName());
            dto.setCustomerEmail(appointment.getUser().getEmail());
        }

        if (appointment.getBranch() != null) {
            dto.setBranchId(appointment.getBranch().getId());
            dto.setBranchName(appointment.getBranch().getName());
        }

        return dto;
    }
}
