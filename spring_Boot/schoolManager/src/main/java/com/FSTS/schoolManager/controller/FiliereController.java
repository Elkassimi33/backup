package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.dtos.FiliereDTO;
import com.FSTS.schoolManager.dtos.FiliereResponseDTO;
import com.FSTS.schoolManager.dtos.ModuleDto;
import com.FSTS.schoolManager.entities.Filiere.NiveauFiliere;
import com.FSTS.schoolManager.service.FiliereService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filieres")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FiliereController {

    private final FiliereService filiereService;

    @PostMapping
    public ResponseEntity<FiliereResponseDTO> createFiliere(@Valid @RequestBody FiliereDTO filiereDTO) {
        return ResponseEntity.ok(filiereService.createFiliere(filiereDTO));
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<FiliereResponseDTO> getFiliereById(@PathVariable Integer id) {
        return filiereService.getFiliereById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FiliereResponseDTO>> getAllFilieres() {
        return ResponseEntity.ok(filiereService.getAllFilieres());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FiliereResponseDTO> updateFiliere(
            @PathVariable Integer id,
            @Valid @RequestBody FiliereDTO filiereDTO) {
        try {
            return ResponseEntity.ok(filiereService.updateFiliere(id, filiereDTO));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiliere(@PathVariable Integer id) {
        try {
            filiereService.deleteFiliere(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/departement/{departementId}")
    public ResponseEntity<List<FiliereResponseDTO>> getFilieresByDepartement(@PathVariable String departementNam) {
        return ResponseEntity.ok(filiereService.getFilieresByDepartement(departementNam));
    }

    @GetMapping("/niveau/{niveau}")
    public ResponseEntity<List<FiliereResponseDTO>> getFilieresByNiveau(@PathVariable NiveauFiliere niveau) {
        return ResponseEntity.ok(filiereService.getFilieresByNiveau(niveau));
    }

    @GetMapping("/active")
    public ResponseEntity<List<FiliereResponseDTO>> getActiveFilieres() {
        return ResponseEntity.ok(filiereService.getActiveFilieres());
    }

    @GetMapping("/{enseignantId}")
    public ResponseEntity<List<FiliereDTO>> getFilieresByEnseignantId(@PathVariable Integer enseignantId) {
        return ResponseEntity.ok(filiereService.getFiliereByEnseignantId(enseignantId));
    }

    @PostMapping("/{filiereId}/chef/{enseignantId}")
    public ResponseEntity<FiliereResponseDTO> assignChefFiliere(
            @PathVariable Integer filiereId,
            @PathVariable Integer enseignantId) {
        try {
            return ResponseEntity.ok(filiereService.assignChefFiliere(filiereId, enseignantId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{filiereId}/chef")
    public ResponseEntity<FiliereResponseDTO> removeChefFiliere(@PathVariable Integer filiereId) {
        try {
            return ResponseEntity.ok(filiereService.removeChefFiliere(filiereId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{filiereId}/modules")
    public ResponseEntity<FiliereResponseDTO> addModule(
            @PathVariable Integer filiereId,
            @Valid @RequestBody ModuleDto moduleDTO) {
        try {
            return ResponseEntity.ok(filiereService.addModule(filiereId, moduleDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{filiereId}/modules/{moduleId}")
    public ResponseEntity<FiliereResponseDTO> removeModule(
            @PathVariable Integer filiereId,
            @PathVariable Integer moduleId) {
        try {
            return ResponseEntity.ok(filiereService.removeModule(filiereId, moduleId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<FiliereResponseDTO> activateFiliere(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(filiereService.activateFiliere(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<FiliereResponseDTO> deactivateFiliere(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(filiereService.deactivateFiliere(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 