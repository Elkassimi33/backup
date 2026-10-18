package com.rsi.patient.patient_service.dtos.responseDtos;

import com.rsi.patient.patient_service.model.embedded.ContactInfo;
import com.rsi.patient.patient_service.model.embedded.EmergencyContact;
import com.rsi.patient.patient_service.model.enums.PatientStatus;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

import com.rsi.patient.patient_service.dtos.AllergyDto;
import com.rsi.patient.patient_service.model.enums.BloodType;
import com.rsi.patient.patient_service.model.enums.Gender;

@Builder
public record PatientResponse(
        UUID id,
        String name,
        Integer age,
        Gender gender,
        BloodType bloodType,
        EmergencyContact emergencyContact,
        List<AllergyDto> allergies,
        PatientStatus status,
        UUID assignedDoctorId,
        UUID assignedNurseId,
        LocalDateTime createdAt,
        ContactInfo contactInfo,
        LocalDate dateOfBirth,
        Integer roomNumber,
        Integer bedNumber,
//        LocalDate admissionDate,
        EmergencyContact emergencyContacts
) {}
