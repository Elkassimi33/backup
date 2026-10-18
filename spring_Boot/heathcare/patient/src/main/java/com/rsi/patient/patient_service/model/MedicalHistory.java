package com.rsi.patient.patient_service.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

import com.rsi.patient.patient_service.model.enums.AllergySeverity;
import com.rsi.patient.patient_service.model.enums.RecordType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medical_history")

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private UUID doctorId;

    @Column(nullable = false)
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    @Enumerated(EnumType.STRING)
    private RecordType recordType;

    private LocalDate date;

    private LocalDate followUpDate;

    @Column(name = "medical_condition")
    @Enumerated(EnumType.STRING)
    private AllergySeverity condition;

    @ElementCollection
    @CollectionTable(name = "history_attachments", joinColumns = @JoinColumn(name = "history_id"))
    private List<String> attachmentUrls = new ArrayList<>();

    public boolean requiresFollowUp() {
        return followUpDate != null && followUpDate.isAfter(LocalDate.now());
    }
}
