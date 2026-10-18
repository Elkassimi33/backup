package com.rsi.patient.patient_service.repositories;

import java.util.List;
import java.util.UUID;

import com.rsi.patient.patient_service.model.enums.PatientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsi.patient.patient_service.model.EmergencyAlert;
import com.rsi.patient.patient_service.model.enums.AlertStatus;

@Repository
public interface EmergencyAlertRepo extends JpaRepository<EmergencyAlert, UUID> {

    List<EmergencyAlert> findByPatientIdAndStatus(UUID patientId, PatientStatus patientStatus);

    List<EmergencyAlert> findByStatusOrderByTimestampDesc(AlertStatus status);

}
