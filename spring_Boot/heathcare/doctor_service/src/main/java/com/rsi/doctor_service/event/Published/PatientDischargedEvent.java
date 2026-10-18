package com.rsi.doctor_service.event.Published;

import java.time.LocalDate;
import java.util.UUID;

public record PatientDischargedEvent(
        UUID patientId, UUID doctorId,
        String summary, LocalDate dischargeDate) {
}
