package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.dtos.*;
import com.FSTS.schoolManager.entities.Filiere;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.entities.Note;
import com.FSTS.schoolManager.service.ChefFiliereService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chef-filiere")
@PreAuthorize("hasAuthority('ROLE_CHEF_FILIERE')")
public class ChefFiliereController {

    private final ChefFiliereService chefFiliereService;

    public ChefFiliereController(ChefFiliereService chefFiliereService) {
        this.chefFiliereService = chefFiliereService;
    }

    @GetMapping("/{enseignantId}/filieres")
    public ResponseEntity<List<Filiere>> getManagedFilieres(@PathVariable Integer enseignantId) {
        return ResponseEntity.ok(chefFiliereService.getManagedFilieres(enseignantId));
    }

    @GetMapping("/filiere/{filiereId}/students")
    public ResponseEntity<List<EtudiantDto>> getFiliereStudents(@PathVariable Integer filiereId) {
        return ResponseEntity.ok(chefFiliereService.getFiliereStudents(filiereId));
    }

    @GetMapping("/module/{moduleId}/statistics")
    public ResponseEntity<GradeStatistics> getModuleGradeStatistics(@PathVariable Long moduleId) {
        return ResponseEntity.ok(chefFiliereService.getModuleGradeStatistics(moduleId));
    }

    @GetMapping("/filiere/{filiereId}/modules")
    public ResponseEntity<List<Module>> getFiliereModules(@PathVariable Integer filiereId) {
        return ResponseEntity.ok(chefFiliereService.getFiliereModules(filiereId));
    }

    @GetMapping("/module/{moduleId}/grades")
    public ResponseEntity<List<noteDto>> getModuleGrades(@PathVariable Long moduleId) {
        return ResponseEntity.ok(chefFiliereService.getModuleGrades(moduleId));
    }

    @PostMapping("/note/{noteId}/validate")
    public ResponseEntity<Void> validateGrade(@PathVariable Integer noteId, @RequestParam boolean validated) {
        chefFiliereService.validateGrade(noteId, validated);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filiere/{filiereId}/statistics")
    public ResponseEntity<List<StudentStatistics>> getFiliereStudentStatistics(@PathVariable Integer filiereId) {
        return ResponseEntity.ok(chefFiliereService.getFiliereStudentStatistics(filiereId));
    }

    @GetMapping("/filiere/{filiereId}/failed-students")
    public ResponseEntity<List<EtudiantDto>> getFailedStudents(@PathVariable Integer filiereId) {
        return ResponseEntity.ok(chefFiliereService.getFailedStudents(filiereId));
    }

    @GetMapping("/filiere/{filiereId}/at-risk-students")
    public ResponseEntity<List<EtudiantDto>> getAtRiskStudents(@PathVariable Integer filiereId) {
        return ResponseEntity.ok(chefFiliereService.getAtRiskStudents(filiereId));
    }

    @GetMapping("/filiere/{filiereId}/modules/display-status")
    public ResponseEntity<List<ModuleDisplayStatusDto>> getModulesWithDisplayStatus(@PathVariable Integer filiereId) {
        return ResponseEntity.ok(chefFiliereService.getModulesWithDisplayStatus(filiereId));
    }
} 