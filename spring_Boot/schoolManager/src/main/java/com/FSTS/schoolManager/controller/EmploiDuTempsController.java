package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.dtos.EmploiDuTempsDto;
import com.FSTS.schoolManager.service.EmploiDuTempsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/v1/emploi-du-temps")
@RequiredArgsConstructor
public class EmploiDuTempsController {

    private final EmploiDuTempsService emploiDuTempsService;

    @PostMapping
    public ResponseEntity<EmploiDuTempsDto> createEmploiDuTemps(@RequestBody EmploiDuTempsDto dto) {
        return ResponseEntity.ok(emploiDuTempsService.createEmploiDuTemps(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmploiDuTempsDto> updateEmploiDuTemps(
            @PathVariable Long id, 
            @RequestBody EmploiDuTempsDto dto) {
        return ResponseEntity.ok(emploiDuTempsService.updateEmploiDuTemps(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploiDuTemps(@PathVariable Long id) {
        emploiDuTempsService.deleteEmploiDuTemps(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<EmploiDuTempsDto>> getEmploiDuTempsByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(emploiDuTempsService.getEmploiDuTempsByModule(moduleId));
    }

    @GetMapping("/salle/{salleId}")
    public ResponseEntity<List<EmploiDuTempsDto>> getEmploiDuTempsBySalle(@PathVariable Integer salleId) {
        return ResponseEntity.ok(emploiDuTempsService.getEmploiDuTempsBySalle(salleId));
    }

    @GetMapping("/jour/{jour}")
    public ResponseEntity<List<EmploiDuTempsDto>> getEmploiDuTempsByJour(@PathVariable DayOfWeek jour) {
        return ResponseEntity.ok(emploiDuTempsService.getEmploiDuTempsByJour(jour));
    }
} 