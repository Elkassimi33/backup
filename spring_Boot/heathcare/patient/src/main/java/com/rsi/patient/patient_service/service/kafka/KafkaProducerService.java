package com.rsi.patient.patient_service.service.kafka;


import com.rsi.patient.patient_service.KafkaConfig;
import com.rsi.patient.patient_service.events.publishe.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publish Patient Registered Event
     */
    public void publishPatientRegistered(PatientRegisteredEvent event) {
        sendEvent(KafkaConfig.TOPIC_PATIENT_REGISTERED, event.patientId().toString(), event);
    }

    /**
     * Publish Medical History Updated Event
     */
    public void publishMedicalHistoryUpdated(MedicalHistoryUpdatedEvent event) {
        sendEvent(KafkaConfig.TOPIC_MEDICAL_HISTORY_UPDATED, event.patientId().toString(), event);
    }

    /**
     * Publish Emergency Triggered Event
     */
    public void publishEmergencyTriggered(EmergencyTriggeredEvent event) {
        sendEvent(KafkaConfig.TOPIC_EMERGENCY_TRIGGERED, event.patientId().toString(), event);
        log.warn(" EMERGENCY EVENT PUBLISHED for patient: {}", event.patientId());
    }

    /**
     * Publish Patient Discharged Event
     */
    public void publishPatientDischarged(String patientId, PatientDischargedEvent event) {
        sendEvent(KafkaConfig.TOPIC_PATIENT_DISCHARGED, patientId, event);
    }

    /**
     * Publish High Risk Detected Event
     */
    public void publishHighRiskDetected(HighRiskDetectedEvent event) {
        sendEvent(KafkaConfig.TOPIC_HIGH_RISK_DETECTED, event.getPatientId().toString(), event);
    }

    /**
     * Generic method to send events with callback
     */

    private void sendEvent(String topic, String key, Object event) {
        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        RecordMetadata metadata = result.getRecordMetadata();
                        log.debug("Event sent successfully to topic: {}, partition: {}, offset: {}",
                                metadata.topic(),
                                metadata.partition(),
                                metadata.offset());
                    } else {
                        log.error("Failed to send event to topic: {}, error: {}",
                                topic, ex.getMessage(), ex);
                    }
                });
    }
}
