package com.rsi.patient.patient_service.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsi.patient.patient_service.model.Patient;
import com.rsi.patient.patient_service.model.enums.PatientStatus;

@Repository
public interface PatientRepo extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByName(String name);


    List<Patient> findByAssignedDoctorId(UUID doctorId);

    List<Patient> findByStatus(PatientStatus status);

    boolean existsByNameAndDateOfBirth(String name, LocalDate dateOfBirth);

}
