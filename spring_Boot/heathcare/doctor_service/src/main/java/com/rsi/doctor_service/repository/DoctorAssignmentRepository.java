package com.rsi.doctor_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsi.doctor_service.model.Doctor;
import com.rsi.doctor_service.model.DoctorAssignment;
import com.rsi.doctor_service.model.enums.AssignmentStatus;

@Repository

public interface DoctorAssignmentRepository extends JpaRepository<DoctorAssignment, UUID> {
    List<DoctorAssignment> findByDoctorAndStatus(Doctor doctor, AssignmentStatus status);

    Optional<DoctorAssignment> findByPatientIdAndStatus(UUID patientId, AssignmentStatus status);

    boolean existsByPatientIdAndStatus(UUID patientId, AssignmentStatus status);
}
