package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.entities.Inscription;
import com.FSTS.schoolManager.service.InscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @PostMapping
    public ResponseEntity<Inscription> createInscription(
            @RequestParam Integer etudiantId,
            @RequestParam Integer anneeId,
            @RequestParam Integer filiereId) {
        return ResponseEntity.ok(inscriptionService.createInscription(etudiantId, anneeId, filiereId));
    }

    @GetMapping("/student/{etudiantId}/year/{anneeId}")
    public ResponseEntity<Inscription> getInscription(
            @PathVariable Integer etudiantId,
            @PathVariable Integer anneeId) {
        return ResponseEntity.ok(inscriptionService.getInscription(etudiantId, anneeId));
    }

    @GetMapping("/student/{etudiantId}")
    public ResponseEntity<List<Inscription>> getStudentInscriptions(
            @PathVariable Integer etudiantId) {
        return ResponseEntity.ok(inscriptionService.getStudentInscriptions(etudiantId));
    }

    @GetMapping("/year/{anneeId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Inscription>> getYearInscriptions(
            @PathVariable Integer anneeId) {
        return ResponseEntity.ok(inscriptionService.getYearInscriptions(anneeId));
    }

    @GetMapping("/check/{etudiantId}/year/{anneeId}")
    public ResponseEntity<Boolean> isStudentEnrolled(
            @PathVariable Integer etudiantId,
            @PathVariable Integer anneeId) {
        return ResponseEntity.ok(inscriptionService.isStudentEnrolled(etudiantId, anneeId));
    }
} 