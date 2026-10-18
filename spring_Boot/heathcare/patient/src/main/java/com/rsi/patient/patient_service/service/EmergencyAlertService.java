package com.rsi.patient.patient_service.service;

import com.rsi.patient.patient_service.dtos.requestDtos.EmergencyAlertRequest;
import com.rsi.patient.patient_service.events.publishe.EmergencyTriggeredEvent;
import com.rsi.patient.patient_service.exception.PatientNotFoundException;
import com.rsi.patient.patient_service.model.EmergencyAlert;
import com.rsi.patient.patient_service.model.Patient;
import com.rsi.patient.patient_service.model.enums.AlertStatus;
import com.rsi.patient.patient_service.model.enums.AlertTrigger;
import com.rsi.patient.patient_service.model.enums.AllergySeverity;
import com.rsi.patient.patient_service.repositories.EmergencyAlertRepo;
import com.rsi.patient.patient_service.repositories.PatientRepo;
import com.rsi.patient.patient_service.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyAlertService {

    private final EmergencyAlertRepo alertRepository;
    private final PatientRepo patientRepository;
    private final KafkaProducerService kafkaProducerService;
    private static final String TOPIC_EMERGENCY_TRIGGERED = "emergency.triggered";

    @Transactional
    public void triggerEmergency(EmergencyAlertRequest request) {
        log.warn("EMERGENCY triggered for patient: {}", request.patientId());

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        EmergencyAlert alert = EmergencyAlert.builder()
                .patient(patient)
                .triggeredBy(request.triggeredBy() != null ? request.triggeredBy() : AlertTrigger.PANIC_BUTTON)
                .severity(AllergySeverity.SEVERE)
                .status(AlertStatus.TRIGGERED)
                .location(request.location())
                .roomNumber(request.roomNumber())
                .floor(request.floor())
                .heartRate(request.heartRate())
                .bloodPressureSystolic(request.bloodPressureSystolic())
                .bloodPressureDiastolic(request.bloodPressureDiastolic())
                .oxygenLevel(request.oxygenLevel())
                .temperature(request.temperature())
                .triggeredAt(LocalDateTime.now())
                .isFalseAlarm(false)
                .build();

        alertRepository.save(alert);

        // Publish event for other services
        publishEmergencyEvent(alert, patient);
    }

    private void publishEmergencyEvent(EmergencyAlert alert, Patient patient) {
        EmergencyTriggeredEvent event = EmergencyTriggeredEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patientId(patient.getId())
                .severity(alert.getSeverity())
                .location(alert.getLocation())
                .roomNumber(alert.getRoomNumber() != null ? "Room " + alert.getRoomNumber() : null)
                .heartRate(alert.getHeartRate())
                .oxygenLevel(alert.getOxygenLevel())
                .triggeredAt(alert.getTriggeredAt())
                .triggerSource(alert.getTriggeredBy())
                .build();

        kafkaProducerService.publishEmergencyTriggered(event);

        log.info("Published EmergencyTriggeredEvent for patient: {}", patient.getId());
    }
}