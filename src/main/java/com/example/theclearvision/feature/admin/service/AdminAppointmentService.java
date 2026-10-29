package com.example.theclearvision.feature.admin.service;

import com.example.theclearvision.feature.appointment.dto.AppointmentDTO;
import java.util.List;

public interface AdminAppointmentService {
    List<AppointmentDTO> getAppointments(String search, String status, String startDate, String endDate, String sortBy,
            String sortDir);

    AppointmentDTO getAppointmentDetail(Long id);

    AppointmentDTO updateAppointmentStatus(Long id, String status);

    void deleteAppointment(Long id);
}
