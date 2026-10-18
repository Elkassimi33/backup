package com.rsi.doctor_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsi.doctor_service.model.Prescription;
import com.rsi.doctor_service.model.enums.PrescriptionStatus;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {
    List<Prescription> findByPatientIdOrderByPrescribedAtDesc(UUID patientId);

    List<Prescription> findByPatientIdAndStatus(UUID patientId, PrescriptionStatus status);

    List<Prescription> findByDoctorId(UUID doctorId);
}
