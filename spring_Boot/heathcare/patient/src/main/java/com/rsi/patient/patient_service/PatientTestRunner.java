//package com.rsi.patient.patient_service;
//
//import com.rsi.patient.patient_service.dtos.AllergyDto;
//import com.rsi.patient.patient_service.dtos.requestDtos.PatientRegistrationRequest;
//import com.rsi.patient.patient_service.model.embedded.EmergencyContact;
//import com.rsi.patient.patient_service.model.enums.AllergySeverity;
//import com.rsi.patient.patient_service.model.enums.BloodType;
//import com.rsi.patient.patient_service.model.enums.Gender;
//import com.rsi.patient.patient_service.service.PatientService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class PatientTestRunner implements CommandLineRunner {
//
//    private final PatientService patientService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        log.info("=== Starting Patient Registration Tests ===");
//
//        testPatientWithAllergies();
//        testPatientWithoutAllergies();
//        testPatientWithEmptyAllergies();
//
//        log.info("=== Patient Registration Tests Completed ===");
//    }
//
//    private void testPatientWithAllergies() {
//        log.info("\n--- Test 1: Patient with Allergies ---");
//
//        EmergencyContact emergencyContact = new EmergencyContact();
//        emergencyContact.setEmergencyName("John Doe");
//        emergencyContact.setRelationShip("Father");
//        emergencyContact.setPhoneNumber("+1234567890");
//
//        AllergyDto allergy1 = AllergyDto.builder()
//                .substance("Penicillin")
//                .severity(AllergySeverity.SEVERE)
//                .reaction("Hives, difficulty breathing")
//                .diagnosedOn(LocalDate.of(2020, 5, 15))
//                .build();
//
//        AllergyDto allergy2 = AllergyDto.builder()
//                .substance("Peanuts")
//                .severity(AllergySeverity.MODERATE)
//                .reaction("Swelling, rash")
//                .diagnosedOn(LocalDate.of(2019, 8, 20))
//                .build();
//
//        AllergyDto allergy3 = AllergyDto.builder()
//                .substance("Latex")
//                .severity(AllergySeverity.MILD)
//                .reaction("Skin irritation")
//                .diagnosedOn(LocalDate.of(2021, 3, 10))
//                .build();
//
//        PatientRegistrationRequest request = new PatientRegistrationRequest(
//                "John Smith",
//                LocalDate.of(1990, 6, 15),
//                Gender.MALE,
//                BloodType.O_POS,
//                emergencyContact,
//                List.of(allergy1, allergy2, allergy3)
//        );
//
//        // Save patient
//        var savedPatient = patientService.registerPatient(request);
//
//        log.info("✓ Patient saved with ID: {}", savedPatient.id());
//        log.info("✓ Patient name: {}", savedPatient.name());
//        log.info("✓ Number of allergies: {}", savedPatient.allergies().size());
//
//        savedPatient.allergies().forEach(allergy -> {
//            log.info("  - Allergy: {} (Severity: {}, Reaction: {})",
//                    allergy.substance(),
//                    allergy.severity(),
//                    allergy.reaction());
//        });
//    }
//
//    private void testPatientWithoutAllergies() {
//        log.info("\n--- Test 2: Patient without Allergies (null) ---");
//
//        EmergencyContact emergencyContact = new EmergencyContact();
//        emergencyContact.setEmergencyName("Jane Doe");
//        emergencyContact.setRelationShip("Mother");
//        emergencyContact.setPhoneNumber("+9876543210");
//
//        PatientRegistrationRequest request = new PatientRegistrationRequest(
//                "Alice Johnson",
//                LocalDate.of(1985, 3, 22),
//                Gender.FEMALE,
//                BloodType.A_POS,
//                emergencyContact,
//                null
//        );
//
//        var savedPatient = patientService.registerPatient(request);
//
//        log.info("✓ Patient saved with ID: {}", savedPatient.id());
//        log.info("✓ Patient name: {}", savedPatient.name());
//        log.info("✓ Allergies list: {}", savedPatient.allergies());
//        log.info("✓ Allergies size: {}", savedPatient.allergies().size());
//    }
//
//    private void testPatientWithEmptyAllergies() {
//        log.info("\n--- Test 3: Patient with Empty Allergies List ---");
//
//        PatientRegistrationRequest request = new PatientRegistrationRequest(
//                "Bob Wilson",
//                LocalDate.of(1978, 11, 5),
//                Gender.MALE,
//                BloodType.B_POS,
//                null,
//                List.of()
//        );
//
//        var savedPatient = patientService.registerPatient(request);
//
//        log.info("✓ Patient saved with ID: {}", savedPatient.id());
//        log.info("✓ Patient name: {}", savedPatient.name());
//        log.info("✓ Allergies size: {}", savedPatient.allergies().size());
//    }
//}