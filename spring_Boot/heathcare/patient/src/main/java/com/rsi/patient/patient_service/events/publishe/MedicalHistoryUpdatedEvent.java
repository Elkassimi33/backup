package com.rsi.patient.patient_service.events.publishe;


import java.time.LocalDateTime;
import java.util.UUID;

public record MedicalHistoryUpdatedEvent (
        UUID patientId,
        UUID doctorId,
        String diagnosis,
        LocalDateTime time
){


}
