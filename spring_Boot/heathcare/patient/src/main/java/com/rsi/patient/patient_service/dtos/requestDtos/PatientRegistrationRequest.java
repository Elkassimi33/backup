package com.rsi.patient.patient_service.dtos.requestDtos;

import java.time.LocalDate;
import java.util.List;

import com.rsi.patient.patient_service.dtos.AllergyDto;
import com.rsi.patient.patient_service.model.embedded.ContactInfo;
import com.rsi.patient.patient_service.model.embedded.EmergencyContact;
import com.rsi.patient.patient_service.model.enums.BloodType;
import com.rsi.patient.patient_service.model.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record PatientRegistrationRequest(

        @NotBlank(message = "First name is required")
        String name,
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,
        @NotNull(message = "Gender is required")
        Gender gender,
        @NotNull BloodType bloodType,
        ContactInfo contentInfo,
        EmergencyContact emergencyContact,
        List<AllergyDto> allergies,
        String insuranceProvider,
        String insurancePolicyNumber
//        String languagePreference

) {}
