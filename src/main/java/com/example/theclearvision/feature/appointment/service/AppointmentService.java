package com.example.theclearvision.feature.appointment.service;

import com.example.theclearvision.feature.appointment.dto.AppointmentDTO;
import com.example.theclearvision.feature.appointment.dto.CreateAppointmentRequest;

import java.util.List;

public interface AppointmentService {
    AppointmentDTO createAppointment(Long userId, CreateAppointmentRequest request);

    List<AppointmentDTO> getUserAppointments(Long userId);

    AppointmentDTO getAppointment(Long id);

    void cancelAppointment(Long id);
}
