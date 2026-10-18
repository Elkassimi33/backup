package com.rsi.doctor_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rsi.doctor_service.dtos.request.AssignmentRequest;
import com.rsi.doctor_service.dtos.request.DischargeRequest;
import com.rsi.doctor_service.dtos.response.AssignmentResponse;
import com.rsi.doctor_service.dtos.response.DoctorResponse;
import com.rsi.doctor_service.event.Published.DoctorAssignedEvent;
import com.rsi.doctor_service.event.Published.PatientDischargedEvent;
import com.rsi.doctor_service.exception.AssignmentNotFoundException;
import com.rsi.doctor_service.exception.DoctorNotFoundException;
import com.rsi.doctor_service.exception.PatientAlreadyAssignedException;
import com.rsi.doctor_service.model.Doctor;
import com.rsi.doctor_service.model.DoctorAssignment;
import com.rsi.doctor_service.model.enums.AssignmentStatus;
import com.rsi.doctor_service.repository.DoctorAssignmentRepository;
import com.rsi.doctor_service.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorAssignmentRepository assignmentRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getMyPatients(UUID doctorId) {
        Doctor doctor = findOrThrow(doctorId);
        return assignmentRepo
                .findByDoctorAndStatus(doctor, AssignmentStatus.ACTIVE)
                .stream()
                .map(this::toAssignmentResponse)
                .toList();
    }

    public AssignmentResponse assignPatient(AssignmentRequest req) {
        Doctor doctor = findOrThrow(req.doctorId());

        // Guard: check not already assigned
        boolean alreadyAssigned = assignmentRepo
                .existsByPatientIdAndStatus(req.patientId(), AssignmentStatus.ACTIVE);

        if (alreadyAssigned) {
            throw new PatientAlreadyAssignedException(
                    "Patient %d already has an active assignment".formatted(req.patientId()));
        }

        DoctorAssignment assignment = DoctorAssignment.builder()
                .doctor(doctor)
                .patientId(req.patientId())
                .nurseId(req.nurseId())
                .patientCondition(req.patientCondition())
                .status(AssignmentStatus.ACTIVE)
                .build();

        DoctorAssignment saved = assignmentRepo.save(assignment);

        // publish event → patient service listens
        eventPublisher.publishEvent(new DoctorAssignedEvent(
                req.patientId(), req.doctorId(), req.nurseId(), LocalDateTime.now()));

        log.info("Patient {} assigned to Doctor {}", req.patientId(), req.doctorId());
        return toAssignmentResponse(saved);
    }

    public void dischargePatient(UUID doctorId, DischargeRequest req) {
        DoctorAssignment assignment = assignmentRepo
                .findByPatientIdAndStatus(req.patientId(), AssignmentStatus.ACTIVE)
                .orElseThrow(() -> new AssignmentNotFoundException(
                        "No active assignment found for patient: " + req.patientId()));

        assignment.discharge(req.summary(), req.instructions());
        assignmentRepo.save(assignment);

        eventPublisher.publishEvent(new PatientDischargedEvent(
                req.patientId(),
                assignment.getDoctor().getId(),
                req.summary(),
                LocalDate.now()));

        log.info("Patient {} discharged", req.patientId());
    }

    public List<DoctorResponse> getAvailableDoctors() {
        return doctorRepository.findAvailableDoctors()
                .stream()
                .map(this::toDoctorResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization)
                .stream()
                .map(this::toDoctorResponse)
                .collect(Collectors.toList());
    }

    public DoctorResponse getDoctorById(UUID doctorId) {
        Doctor doctor = findOrThrow(doctorId);
        return toDoctorResponse(doctor);
    }

    @Transactional(readOnly = true)
    public List<UUID> getMyPatientIds(UUID doctorId) {
        Doctor doctor = findOrThrow(doctorId);
        return assignmentRepo
                .findByDoctorAndStatus(doctor, AssignmentStatus.ACTIVE)
                .stream()
                .map(DoctorAssignment::getPatientId)
                .toList();
    }

    // Helper

    private Doctor findOrThrow(UUID doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found: " + doctorId));

    }

    private AssignmentResponse toAssignmentResponse(DoctorAssignment a) {

        return AssignmentResponse.builder()
                .id(a.getId())
                .patientId(a.getPatientId())
                .doctorName(a.getDoctor().getFullName())
                .nurseId(a.getNurseId())
                .doctorId(a.getDoctor().getId())
                .status(a.getStatus())
                .assignmentDate(a.getAssignmentDate())
                .daysAdmitted(a.getDaysAdmitted())
                .build();
    }

    private DoctorResponse toDoctorResponse(Doctor doctor) {

        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .rating(doctor.getRating())
                .status(doctor.getStatus())
                .department(doctor.getDepartment())
                .licenseNumber(doctor.getLicenseNumber())
                .specialization(doctor.getSpecialization())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .consultationFee(doctor.getConsultationFee())
                .isAvailable(doctor.isAvailable())
                .activePatientCount(doctor.getActivePatientCount())
                .build();
    }
}
