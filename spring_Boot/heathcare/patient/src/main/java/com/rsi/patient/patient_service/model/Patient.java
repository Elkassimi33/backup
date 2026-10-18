package com.rsi.patient.patient_service.model;

import com.rsi.patient.patient_service.model.embedded.ContactInfo;
import com.rsi.patient.patient_service.model.embedded.EmergencyContact;
import com.rsi.patient.patient_service.model.enums.BloodType;
import com.rsi.patient.patient_service.model.enums.Gender;
import com.rsi.patient.patient_service.model.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "patients")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private UUID assignedDoctorId;
    private UUID assignedNurseId;

    Integer roomNumber;
    Integer bedNumber;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private BloodType bloodType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private PatientStatus status;

    @Embedded
    private ContactInfo contactInfo;


    @Embedded
    private EmergencyContact emergencyContact;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "discharge_date")
    private LocalDateTime dischargeDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(name = "admission_reason", columnDefinition = "TEXT")
    private String admissionReason;

    @Column(columnDefinition = "TEXT")
    private String qrCode;

    @Column(name = "insurance_provider", length = 100)
    private String insuranceProvider;

    @Column(name = "insurance_policy_number", length = 100)
    private String insurancePolicyNumber;

    @Builder.Default
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Allergy> allergies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @OrderBy("date DESC")
    private List<MedicalHistory> medicalHistory = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<EmergencyAlert> emergencyAlerts = new ArrayList<>();

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public boolean isMinor() {
        return getAge() < 18;
    }

    public void addAllergy(@NonNull Allergy allergy) {
        allergy.setPatient(this);
        this.allergies.add(allergy);
    }

    public void addMedicalHistory(@NonNull MedicalHistory history) {
        history.setPatient(this);
        this.medicalHistory.add(history);
    }

    public Optional<MedicalHistory> getLastHistory() {
        return medicalHistory.stream().findFirst();
    }

    public boolean isCritical() {
        return this.status == PatientStatus.CRITICAL;
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = PatientStatus.ACTIVE;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}