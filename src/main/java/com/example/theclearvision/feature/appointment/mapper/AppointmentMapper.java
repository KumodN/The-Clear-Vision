package com.example.theclearvision.feature.appointment.mapper;

import com.example.theclearvision.feature.appointment.Appointment;
import com.example.theclearvision.feature.appointment.dto.AppointmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    AppointmentDTO toDTO(Appointment appointment);
}
