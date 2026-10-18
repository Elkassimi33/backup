package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.entities.AnneeUniversitaire;
import com.FSTS.schoolManager.service.AnneeUniversitaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annees-universitaires")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AnneeUniversitaireController {

    private final AnneeUniversitaireService anneeUniversitaireService;

    @PostMapping
    public ResponseEntity<AnneeUniversitaire> createAnneeUniversitaire(@RequestBody AnneeUniversitaire anneeUniversitaire) {
        return ResponseEntity.ok(anneeUniversitaireService.createAnneeUniversitaire(anneeUniversitaire));
    }

    @PutMapping("/{anneeId}")
    public ResponseEntity<AnneeUniversitaire> updateAnneeUniversitaire(
            @PathVariable Integer anneeId,
            @RequestBody AnneeUniversitaire anneeUniversitaire) {
        return ResponseEntity.ok(anneeUniversitaireService.updateAnneeUniversitaire(anneeId, anneeUniversitaire));
    }

    @DeleteMapping("/{anneeId}")
    public ResponseEntity<Void> deleteAnneeUniversitaire(@PathVariable Integer anneeId) {
        anneeUniversitaireService.deleteAnneeUniversitaire(anneeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{anneeId}")
    public ResponseEntity<AnneeUniversitaire> getAnneeUniversitaireById(@PathVariable Integer anneeId) {
        return ResponseEntity.ok(anneeUniversitaireService.getAnneeUniversitaireById(anneeId));
    }

    @GetMapping
    public ResponseEntity<List<AnneeUniversitaire>> getAllAnneesUniversitaires() {
        return ResponseEntity.ok(anneeUniversitaireService.getAllAnneesUniversitaires());
    }

    @GetMapping("/current")
    public ResponseEntity<AnneeUniversitaire> getCurrentAnneeUniversitaire() {
        return ResponseEntity.ok(anneeUniversitaireService.getCurrentAnneeUniversitaire());
    }

    @PutMapping("/{anneeId}/set-current")
    public ResponseEntity<Void> setCurrentAnneeUniversitaire(@PathVariable Integer anneeId) {
        anneeUniversitaireService.setCurrentAnneeUniversitaire(anneeId);
        return ResponseEntity.ok().build();
    }
} 