package com.rsi.patient.patient_service.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsi.patient.patient_service.model.MedicalHistory;

@Repository
public interface MediclaHistoryRepo extends JpaRepository<MedicalHistory, UUID> {

    List<MedicalHistory> findByPatientIdOrderByDateDesc(UUID patientId);

    List<MedicalHistory> findByDoctorId(UUID doctorId);

}
