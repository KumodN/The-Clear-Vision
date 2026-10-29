package com.example.theclearvision.feature.appointment.repository;

import com.example.theclearvision.feature.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    List<Appointment> findByUserId(Long userId);

    @Query("SELECT a FROM Appointment a WHERE a.branch.id = :branchId AND a.appointmentTime = :appointmentTime AND a.status != 'CANCELLED'")
    List<Appointment> findConflictingAppointments(@Param("branchId") Long branchId,
            @Param("appointmentTime") LocalDateTime appointmentTime);
}
