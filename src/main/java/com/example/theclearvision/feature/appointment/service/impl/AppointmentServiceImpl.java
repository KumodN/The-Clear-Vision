package com.example.theclearvision.feature.appointment.service.impl;

import com.example.theclearvision.common.exception.BusinessException;
import com.example.theclearvision.common.exception.ResourceNotFoundException;
import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.appointment.Appointment;
import com.example.theclearvision.feature.appointment.AppointmentStatus;
import com.example.theclearvision.feature.appointment.dto.AppointmentDTO;
import com.example.theclearvision.feature.appointment.dto.CreateAppointmentRequest;
import com.example.theclearvision.feature.appointment.mapper.AppointmentMapper;
import com.example.theclearvision.feature.appointment.repository.AppointmentRepository;
import com.example.theclearvision.feature.appointment.service.AppointmentService;
import com.example.theclearvision.feature.auth.repository.UserRepository;
import com.example.theclearvision.feature.branch.Branch;
import com.example.theclearvision.feature.branch.repository.BranchRepository;
import com.example.theclearvision.feature.doctor.repository.DoctorRepository;
import com.example.theclearvision.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

        private final AppointmentRepository appointmentRepository;
        private final BranchRepository branchRepository;
        private final UserRepository userRepository;
        private final DoctorRepository doctorRepository;
        private final AppointmentMapper appointmentMapper;
        private final EmailService emailService;

        @Override
        @Transactional
        public AppointmentDTO createAppointment(Long userId, CreateAppointmentRequest request) {
                log.info("Creating appointment for user: {}", userId);

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

                Branch branch = branchRepository.findById(request.getBranchId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Branch not found with id: " + request.getBranchId()));

                // Validate appointment time is in the future (with 1-hour buffer for
                // processing)
                if (request.getAppointmentTime().isBefore(java.time.LocalDateTime.now().plusHours(1))) {
                        throw new BusinessException("Appointment time must be at least 1 hour in the future");
                }

                // Get doctor if specified
                com.example.theclearvision.domain.Doctor doctor = null;
                if (request.getDoctorId() != null) {
                        doctor = doctorRepository.findById(request.getDoctorId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Doctor not found with id: " + request.getDoctorId()));
                }

                // Check for conflicts
                List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(request.getBranchId(),
                                request.getAppointmentTime());
                if (!conflicts.isEmpty()) {
                        throw new BusinessException("Time slot is already booked");
                }

                Appointment appointment = Appointment.builder()
                                .user(user)
                                .branch(branch)
                                .doctor(doctor)
                                .appointmentTime(request.getAppointmentTime())
                                .status(AppointmentStatus.SCHEDULED)
                                .notes(request.getNotes())
                                .createdBy(userId)
                                .build();

                Appointment savedAppointment = appointmentRepository.save(appointment);

                // Send email confirmation asynchronously
                try {
                        emailService.sendAppointmentConfirmation(savedAppointment);
                } catch (Exception e) {
                        log.error("Failed to send email notification, but appointment was created", e);
                }

                return appointmentMapper.toDTO(savedAppointment);
        }

        @Override
        @Transactional(readOnly = true)
        public List<AppointmentDTO> getUserAppointments(Long userId) {
                return appointmentRepository.findByUserId(userId).stream()
                                .map(appointmentMapper::toDTO)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public AppointmentDTO getAppointment(Long id) {
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Appointment not found with id: " + id));
                return appointmentMapper.toDTO(appointment);
        }

        @Override
        @Transactional
        public void cancelAppointment(Long id) {
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Appointment not found with id: " + id));

                // Check 24-hour cancellation policy
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                long hoursSinceCreation = java.time.temporal.ChronoUnit.HOURS.between(appointment.getCreatedAt(), now);

                if (hoursSinceCreation > 24) {
                        throw new BusinessException(
                                        "Cannot cancel appointment after 24 hours of booking. Please contact support.");
                }

                appointment.setStatus(AppointmentStatus.CANCELLED);
                appointmentRepository.save(appointment);

                log.info("Appointment {} cancelled successfully", id);
        }
}
