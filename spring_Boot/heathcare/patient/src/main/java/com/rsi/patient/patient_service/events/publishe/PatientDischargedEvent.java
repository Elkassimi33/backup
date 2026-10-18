package com.rsi.patient.patient_service.events.publishe;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDischargedEvent {
    private String eventId;
    private UUID patientId;
    private String patientName;
    private String dischargeSummary;
    private String primaryDoctorId;
    private LocalDateTime dischargeDate;
    private Integer lengthOfStay;
}