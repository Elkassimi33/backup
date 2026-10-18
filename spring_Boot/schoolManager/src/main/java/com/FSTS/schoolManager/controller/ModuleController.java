package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.dtos.ModuleDto;
import com.FSTS.schoolManager.service.ModuleSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleSevice moduleService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createModule(@RequestBody ModuleDto moduleDto) {
        moduleService.insertModule(moduleDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/batch")
    public ResponseEntity<Void> createModules(@RequestBody List<ModuleDto> moduleDtos) {
        moduleService.insertModules(moduleDtos);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{moduleId}")
    public ResponseEntity<ModuleDto> updateModule(@PathVariable Long moduleId, @RequestBody ModuleDto moduleDto) {
        return ResponseEntity.ok(moduleService.updateModule(moduleId, moduleDto));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleDto> getModulesId(@PathVariable Long moduleId) {
        return ResponseEntity.ok(moduleService.getModulesById(moduleId));
    }

    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<List<ModuleDto>> getModulesByEnseignant(@PathVariable Integer enseignantId) {
        return ResponseEntity.ok(moduleService.getModulesByEnseignant(enseignantId));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/departement/{departementId}")
    public ResponseEntity<List<ModuleDto>> getModulesByDepartement(@PathVariable Integer departementId) {
        return ResponseEntity.ok(moduleService.getModulesByDepartement(departementId));
    }

    @GetMapping("/filiere/{filiereId}")
    public ResponseEntity<List<ModuleDto>> getModulesByFiliere(@PathVariable Integer filiereId) {
        return ResponseEntity.ok(moduleService.getModulesByFiliere(filiereId));
    }

    @GetMapping
    public ResponseEntity<List<ModuleDto>> getALLModules() {
        return ResponseEntity.ok(moduleService.getAllModules());
    }

} 