package com.example.theclearvision.feature.appointment.controller;

import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.appointment.dto.AppointmentDTO;
import com.example.theclearvision.feature.appointment.dto.CreateAppointmentRequest;
import com.example.theclearvision.feature.appointment.service.AppointmentService;
import com.example.theclearvision.feature.auth.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody CreateAppointmentRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")); // Should be handled by global exception
                                                                            // handler or custom exception

        AppointmentDTO appointment = appointmentService.createAppointment(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getUserAppointments(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AppointmentDTO> appointments = appointmentService.getUserAppointments(user.getId());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Long id) {
        // TODO: Add security check to ensure user owns this appointment or is admin
        AppointmentDTO appointment = appointmentService.getAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        // TODO: Add security check
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
