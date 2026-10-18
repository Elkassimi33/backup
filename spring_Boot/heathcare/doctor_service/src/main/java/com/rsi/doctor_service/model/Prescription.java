package com.rsi.doctor_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.rsi.doctor_service.model.enums.PrescriptionStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescriptions", indexes = {
        @Index(name = "idx_prescription_patient", columnList = "patient_id"),
        @Index(name = "idx_prescription_doctor", columnList = "doctor_id"),
        @Index(name = "idx_prescription_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private String medication;

    @Column(nullable = false)
    private String dosage; // e.g. "500mg"

    @Column(nullable = false)
    private String frequency; // e.g. "twice daily"

    @Column(columnDefinition = "TEXT")
    private String instructions; // e.g. "take with food"

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate; // null = ongoing

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime prescribedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return status == PrescriptionStatus.ACTIVE
                && (endDate == null || !LocalDate.now().isAfter(endDate));
    }

    public long getRemainingDays() {
        if (endDate == null)
            return -1; // ongoing
        return ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    public void cancel() {
        this.status = PrescriptionStatus.CANCELLED;
    }

    @PrePersist
    private void prePersist() {
        this.prescribedAt = LocalDateTime.now();
        this.status = PrescriptionStatus.ACTIVE;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
