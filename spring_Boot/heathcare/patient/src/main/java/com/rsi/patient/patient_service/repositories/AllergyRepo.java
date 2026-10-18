package com.rsi.patient.patient_service.repositories;

import com.rsi.patient.patient_service.model.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AllergyRepo extends JpaRepository<Allergy, UUID> {
    List<Allergy> findByPatientId(UUID patientId);
    void deleteByPatientId(UUID patientId);
}