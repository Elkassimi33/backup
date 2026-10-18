package com.rsi.patient.patient_service.events.publishe;


import com.rsi.patient.patient_service.model.enums.AllergySeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HighRiskDetectedEvent {
    private String eventId;
    private UUID patientId;
    private String patientName;
    private String condition;
    private Double riskScore;
    private AllergySeverity severity;
    private List<String> recommendations;
    private Boolean requiresImmediateAction;
    private LocalDateTime detectedAt;
}