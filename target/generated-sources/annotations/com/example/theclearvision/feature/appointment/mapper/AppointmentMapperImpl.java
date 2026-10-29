package com.example.theclearvision.feature.appointment.mapper;

import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.appointment.Appointment;
import com.example.theclearvision.feature.appointment.dto.AppointmentDTO;
import com.example.theclearvision.feature.branch.Branch;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T11:35:17+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    @Override
    public AppointmentDTO toDTO(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        AppointmentDTO appointmentDTO = new AppointmentDTO();

        appointmentDTO.setUserId( appointmentUserId( appointment ) );
        appointmentDTO.setBranchId( appointmentBranchId( appointment ) );
        appointmentDTO.setBranchName( appointmentBranchName( appointment ) );
        appointmentDTO.setId( appointment.getId() );
        appointmentDTO.setAppointmentTime( appointment.getAppointmentTime() );
        appointmentDTO.setStatus( appointment.getStatus() );
        appointmentDTO.setNotes( appointment.getNotes() );
        appointmentDTO.setCreatedAt( appointment.getCreatedAt() );

        return appointmentDTO;
    }

    private Long appointmentUserId(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }
        User user = appointment.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long appointmentBranchId(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }
        Branch branch = appointment.getBranch();
        if ( branch == null ) {
            return null;
        }
        Long id = branch.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String appointmentBranchName(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }
        Branch branch = appointment.getBranch();
        if ( branch == null ) {
            return null;
        }
        String name = branch.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
