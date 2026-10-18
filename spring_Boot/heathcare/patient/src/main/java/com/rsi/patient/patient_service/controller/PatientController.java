package com.rsi.patient.patient_service.controller;


import com.rsi.patient.patient_service.dtos.requestDtos.EmergencyAlertRequest;
import com.rsi.patient.patient_service.dtos.requestDtos.MedicalHistoryRequest;
import com.rsi.patient.patient_service.dtos.requestDtos.PatientRegistrationRequest;
import com.rsi.patient.patient_service.dtos.responseDtos.PatientResponse;
import com.rsi.patient.patient_service.dtos.responseDtos.QrCodeResponse;
import com.rsi.patient.patient_service.service.EmergencyAlertService;
import com.rsi.patient.patient_service.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final EmergencyAlertService emergencyAlertService;

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> registerPatient(@Valid @RequestBody PatientRegistrationRequest request) {
        PatientResponse response = patientService.registerPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable UUID id) {
        PatientResponse response = patientService.getProfile(id);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable UUID id,
            @Valid @RequestBody PatientRegistrationRequest request) {
        PatientResponse response = patientService.updatePatient(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{patientId}/assign-doctor")
    public ResponseEntity<Void> assignDoctor(
            @PathVariable UUID patientId,
            @RequestParam UUID doctorId) {
         patientService.onDoctorAssigned(patientId, doctorId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{patientId}/assign-room")
    public ResponseEntity<PatientResponse> assignRoom(
            @PathVariable UUID patientId,
            @RequestParam Integer roomNumber,
            @RequestParam Integer bedNumber) {
        PatientResponse response = patientService.assignRoom(patientId, roomNumber, bedNumber);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{patientId}/discharge")
    public ResponseEntity<Void> dischargePatient(
            @PathVariable UUID patientId,
            @RequestParam String dischargeSummary) {
        patientService.dischargePatient(patientId, dischargeSummary);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/qr/{patientId}")
    public ResponseEntity<QrCodeResponse> getPatientQrCode(@PathVariable UUID patientId) {
        QrCodeResponse response = patientService.generateQr(patientId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PatientResponse>> getPatientsByDoctor(@PathVariable UUID doctorId) {
        List<PatientResponse> patients = patientService.getPatientsByDoctor(doctorId);
        return ResponseEntity.ok(patients);
    }

    @PostMapping("/emergency")
    public ResponseEntity<Void> triggerEmergency(@Valid @RequestBody EmergencyAlertRequest request) {
        emergencyAlertService.triggerEmergency(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/history/{patientId}")
    public ResponseEntity<Void> updateHistory(
            @PathVariable UUID patientId,
            @Valid @RequestBody MedicalHistoryRequest request) {
        patientService.updateHistory(patientId, request);
        return ResponseEntity.accepted().build();
    }


}