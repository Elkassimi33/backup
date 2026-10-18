package com.rsi.patient.patient_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.rsi.patient.patient_service.events.publishe.HighRiskDetectedEvent;
import com.rsi.patient.patient_service.events.publishe.MedicalHistoryUpdatedEvent;
import com.rsi.patient.patient_service.events.publishe.PatientRegisteredEvent;
import com.rsi.patient.patient_service.model.enums.AllergySeverity;
import com.rsi.patient.patient_service.model.enums.PatientStatus;
import com.rsi.patient.patient_service.service.kafka.KafkaProducerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.rsi.patient.patient_service.dtos.AllergyDto;
import com.rsi.patient.patient_service.dtos.requestDtos.MedicalHistoryRequest;
import com.rsi.patient.patient_service.dtos.requestDtos.PatientRegistrationRequest;
import com.rsi.patient.patient_service.dtos.responseDtos.PatientResponse;
import com.rsi.patient.patient_service.dtos.responseDtos.QrCodeResponse;
import com.rsi.patient.patient_service.exception.PatientNotFoundException;
import com.rsi.patient.patient_service.model.Allergy;
import com.rsi.patient.patient_service.model.MedicalHistory;
import com.rsi.patient.patient_service.model.Patient;
import com.rsi.patient.patient_service.repositories.MediclaHistoryRepo;
import com.rsi.patient.patient_service.repositories.PatientRepo;
import com.rsi.patient.patient_service.utils.QrCodeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepo patientRep;
    private final MediclaHistoryRepo mediclaHistoryRepo;
    private final QrCodeService qrCodeService;
    private final KafkaProducerService kafkaProducerService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_PATIENT_REGISTERED = "patient.registered";

    public PatientResponse registerPatient(PatientRegistrationRequest request) {


        Patient patient = Patient.builder()
        .name(request.name())
        .contactInfo(request.contentInfo())
        .emergencyContact(request.emergencyContact())
        .dateOfBirth(request.dateOfBirth())
        .insuranceProvider(request.insuranceProvider())
        .insurancePolicyNumber(request.insurancePolicyNumber())
        .gender(request.gender())
        .bloodType(request.bloodType())
        .insuranceProvider(request.insuranceProvider())
        .insurancePolicyNumber(request.insurancePolicyNumber())
        .build();

        if (request.allergies() != null) {
            request.allergies().forEach(a -> patient.addAllergy( mapToAllergy(a)));
        }

        Patient saved = patientRep.save(patient);

        String qrContent = "http://smarthealth.app/patient/" + saved.getId();
        String qrBase64 = qrCodeService.generateBase64(qrContent);
        saved.setQrCode(qrBase64);

        patientRep.save(saved);

        log.info("Patient registered: id={}", saved.getId());

        // publish event

        kafkaProducerService.publishPatientRegistered( new PatientRegisteredEvent(
                java.util.UUID.randomUUID(), saved.getId(), saved.getName(), LocalDateTime.now()));
        log.info("Published PatientRegisteredEvent for patient: {}", saved.getId());


        return toResponse(saved);

    }

    @Transactional
    @CacheEvict(value = "patients", key = "#id")
    public PatientResponse updatePatient(UUID patientId, PatientRegistrationRequest request) {
        log.info("Updating patient: {}", patientId);

        Patient patient = findOrThrow(patientId);

        patient.setName(request.name());
        patient.setContactInfo(request.contentInfo());
        patient.setEmergencyContact(request.emergencyContact());
        patient.setGender(request.gender());
        patient.setBloodType(request.bloodType());
        patient.setInsuranceProvider(request.insuranceProvider());
        patient.setInsurancePolicyNumber(request.insurancePolicyNumber());

        if (request.allergies() != null) {
            request.allergies().forEach(a -> patient.addAllergy( mapToAllergy(a)));
        }

        Patient updatedPatient = patientRep.save(patient);

        return toResponse(updatedPatient);
    }


    @Transactional
    public PatientResponse assignRoom(UUID patientId, Integer roomNumber, Integer bedNumber) {
        Patient patient = findOrThrow(patientId);

        patient.setRoomNumber(roomNumber);
        patient.setBedNumber(bedNumber);

        Patient updatedPatient = patientRep.save(patient);

        return toResponse(updatedPatient);
    }

    @Cacheable(value = "patients", key = "#id")
    @Transactional()
    public PatientResponse getProfile(UUID patientId) {
        Patient patient = findOrThrow(patientId);
        return toResponse(patient);
    }

    public void updateHistory(UUID patientId, MedicalHistoryRequest request) {
        Patient patient = findOrThrow(patientId);

        MedicalHistory history = MedicalHistory.builder()
                .doctorId(request.doctorId())
                .diagnosis(request.diagnosis())
                .treatment(request.treatment())
                .clinicalNotes(request.clinicalNotes())
                .recordType(request.recordType())
                .date(LocalDate.now())
                .condition(request.condition())
                .followUpDate(request.followUpDate())
                .attachmentUrls(request.attachmentUrls())
                .build();

        patient.addMedicalHistory(history);
        patientRep.save(patient);

        log.info("Medical history update for patient id={}", patientId);

        kafkaProducerService.publishMedicalHistoryUpdated(new MedicalHistoryUpdatedEvent(
                patientId, request.doctorId(), request.diagnosis(), LocalDateTime.now()
        ));

        if (request.condition().equals(AllergySeverity.SEVERE)) {
            publishHighRiskEvent(patient, history);
        }

    }

    @Transactional
    public void dischargePatient(UUID patientId, String dischargeSummary) {
        Patient patient = findOrThrow(patientId);

        patient.setStatus(PatientStatus.DISCHARGED);
        patient.setDischargeDate(LocalDateTime.now());
        patient.setAdmissionReason(dischargeSummary);

        patientRep.save(patient);

        log.info("Patient discharged: {}", patientId);
    }


    @Transactional(readOnly = true)
    public QrCodeResponse generateQr(UUID patientId) {
        Patient patient = findOrThrow(patientId);
        String content = "http://smarthealth.app/patient/" + patientId;
        return new QrCodeResponse(patientId, patient.getQrCode(), content);
    }

    // Handele Consumed Events

    public void onDoctorAssigned(UUID patientId, UUID doctorId) {
        Patient patient = findOrThrow(patientId);
        patient.setAssignedDoctorId(doctorId);
        patientRep.save(patient);
    }

    public void onNurseAssigned(UUID patientId, UUID nurseId) {
        Patient patient = findOrThrow(patientId);
        patient.setAssignedNurseId(nurseId);
        patientRep.save(patient);
    }

    public List<PatientResponse> getPatientsByDoctor(UUID doctorId) {
        return patientRep.findByAssignedDoctorId(doctorId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Helpers


//    private String generateQRData(Patient patient) {
//        return String.format(
//                "PATIENT_ID:%s|NAME:%s %s|DOB:%s|BLOOD:%s",
//                patient.getId(),
//                patient.getName(),
//                patient.getDateOfBirth(),
//                patient.getBloodType() != null ? patient.getBloodType() : "Unknown"
//        );
//    }
    private Patient findOrThrow(UUID id) {
        return patientRep.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(
                        "Patient not found: " + id));
    }

    private void publishHighRiskEvent(Patient patient, MedicalHistory medicalHistory) {
        HighRiskDetectedEvent event = HighRiskDetectedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patientId(patient.getId())
                .patientName(patient.getName())
                .condition(medicalHistory.getDiagnosis())
                .riskScore(0.85)
//                .severity(medicalHistory.getSeverity())
                .recommendations(List.of("Immediate specialist consultation", "ICU admission recommended"))
                .requiresImmediateAction(true)
                .detectedAt(LocalDateTime.now())
                .build();

        kafkaProducerService.publishHighRiskDetected(event);
    }


    private PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .bloodType(patient.getBloodType())
                .emergencyContact((patient.getEmergencyContact()))
                .allergies(patient.getAllergies().stream(). map(this:: mapToAllergyDto).toList())
                .status(patient.getStatus())
                .assignedDoctorId(patient.getAssignedDoctorId())
                .assignedNurseId(patient.getAssignedNurseId())
                .createdAt(patient.getCreatedAt())
                .contactInfo(patient.getContactInfo())
                .dateOfBirth(patient.getDateOfBirth())
                .roomNumber(patient.getRoomNumber())
                .bedNumber(patient.getBedNumber())
                .build();
    }

    private AllergyDto mapToAllergyDto(Allergy allergy) {
        return AllergyDto.builder()
                .patientId(allergy.getPatient().getId())
                .id(allergy.getId())
                .diagnosedOn(allergy.getDiagnosedOn())
                .reaction(allergy.getReaction())
                .substance(allergy.getSubstance())
                .severity(allergy.getSeverity())
                .build();
    }

    private Allergy mapToAllergy(AllergyDto allergyDto) {

        return Allergy.builder()
                .reaction(allergyDto.reaction())
                .substance(allergyDto.substance())
                .severity(allergyDto.severity())
                .diagnosedOn(allergyDto.diagnosedOn())
                .build();

    }

}
