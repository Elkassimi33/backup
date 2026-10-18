package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.dtos.DepartementDto;
import com.FSTS.schoolManager.service.DepartementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departements")
@RequiredArgsConstructor
public class DepartementController {

    private final DepartementService departementService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<DepartementDto> createDepartement(@RequestBody DepartementDto departementDtoS) throws JsonProcessingException {
        return ResponseEntity.ok(departementService.createDepartement(departementDtoS));
    }

    @PutMapping("/{departementId}")
    public ResponseEntity<DepartementDto> updateDepartement(
            @PathVariable Integer departementId,
            @RequestBody DepartementDto departementDto) {
        return ResponseEntity.ok(departementService.updateDepartement(departementId, departementDto));
    }

    @DeleteMapping("/{departementId}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Integer departementId) {
        departementService.deleteDepartement(departementId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{departementId}")
    public ResponseEntity<DepartementDto> getDepartementById(@PathVariable Integer departementId) {
        return ResponseEntity.ok(departementService.getDepartementById(departementId));
    }

    @GetMapping
    public ResponseEntity<List<DepartementDto>> getAllDepartements() {
        return ResponseEntity.ok(departementService.getAllDepartements());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<DepartementDto> getDepartementByNom(@PathVariable String nom) {
        return ResponseEntity.ok(departementService.getDepartementByNom(nom));
    }


} 