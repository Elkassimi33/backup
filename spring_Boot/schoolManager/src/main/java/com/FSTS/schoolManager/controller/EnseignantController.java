package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.dtos.EnseignantDto;
import com.FSTS.schoolManager.entities.Enseignant;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
@RequiredArgsConstructor
public class EnseignantController {

    private final EnseignantService enseignantService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<EnseignantDto> getEnseignantByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(enseignantService.getEnseignantByUserId(userId));
    }

    @PutMapping("/{enseignantId}")
    public ResponseEntity<EnseignantDto> updateEnseignant(
            @PathVariable Integer enseignantId,
            @RequestPart("enseignant") EnseignantDto enseignantDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(enseignantService.updateEnseignant(enseignantId, enseignantDto, file));
    }

    @GetMapping("/{enseignantId}/modules")
    public ResponseEntity<List<Module>> getModulesTaughtThisYear(@PathVariable Integer enseignantId) {
        return ResponseEntity.ok(enseignantService.modulesTaughtThisYear(enseignantId));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/departement/{departementName}")
    public ResponseEntity<List<EnseignantDto>> getEnseignantsByDepartement(@PathVariable String departementName) {
        return ResponseEntity.ok(enseignantService.getDepartementEnseignant(departementName));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<EnseignantDto>> getEnseignantsByStatut(@PathVariable Enseignant.StatutEnseignant statut) {
        return ResponseEntity.ok(enseignantService.findByStatut(statut));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/specialite/{specialite}")
    public ResponseEntity<List<EnseignantDto>> getEnseignantsBySpecialite(@PathVariable String specialite) {
        return ResponseEntity.ok(enseignantService.findBySpecialiteContainingIgnoreCase(specialite));
    }
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<EnseignantDto>> getAllEnseignants() {
        return ResponseEntity.ok(enseignantService.getAllEnseignants());
    }
}