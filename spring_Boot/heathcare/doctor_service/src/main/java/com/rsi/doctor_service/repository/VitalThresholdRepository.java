package com.rsi.doctor_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsi.doctor_service.model.VitalThreshold;

@Repository
public interface VitalThresholdRepository extends JpaRepository<VitalThreshold, UUID> {
    Optional<VitalThreshold> findByPatientId(UUID patientId);

    List<VitalThreshold> findByDoctorId(UUID doctorId);
}
