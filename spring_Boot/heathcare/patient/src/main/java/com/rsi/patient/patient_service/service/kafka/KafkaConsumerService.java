package com.rsi.patient.patient_service.service.kafka;

import com.rsi.patient.patient_service.KafkaConfig;
import com.rsi.patient.patient_service.events.consumed.DoctorAssignedEvent;
import com.rsi.patient.patient_service.events.consumed.NurseAssignedEvent;
import com.rsi.patient.patient_service.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final PatientService patientService;

    @KafkaListener(
            topics = KafkaConfig.TOPIC_DOCTOR_ASSIGNED,
            groupId = "patient-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeDoctorAssignedEvent(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        try {
            DoctorAssignedEvent event = (DoctorAssignedEvent) record.value();
            log.info("Received DoctorAssignedEvent for patient: {}, doctor: {}",
                    event.patientId(), event.doctorId());

            // Update patient with assigned doctor
            patientService.onDoctorAssigned(
                    event.patientId(),
                    event.doctorId()
            );

            ack.acknowledge();
            log.debug("DoctorAssignedEvent processed successfully");

        } catch (Exception e) {
            log.error("Error processing DoctorAssignedEvent: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = KafkaConfig.TOPIC_NURSE_ASSIGNED,
            groupId = "patient-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeNurseAssignedEvent(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        try {
            NurseAssignedEvent event = (NurseAssignedEvent) record.value();
            log.info("Received NurseAssignedEvent for patient: {}, nurses: {}",
                    event.patientId(), event.nurseId());

            patientService.onNurseAssigned(event.patientId(), event.nurseId());

            ack.acknowledge();
            log.debug("NurseAssignedEvent processed successfully");

        } catch (Exception e) {
            log.error("Error processing NurseAssignedEvent: {}", e.getMessage(), e);
        }
    }
}