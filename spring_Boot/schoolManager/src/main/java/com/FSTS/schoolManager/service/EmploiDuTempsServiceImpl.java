package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.EmploiDuTempsDto;
import com.FSTS.schoolManager.entities.AnneeUniversitaire;
import com.FSTS.schoolManager.entities.EmploiDuTemps;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.entities.Salle;
import com.FSTS.schoolManager.exception.*;
import com.FSTS.schoolManager.repositories.AnneeUniversitaireRepository;
import com.FSTS.schoolManager.repositories.EmploiDuTempsRepository;
import com.FSTS.schoolManager.repositories.ModuleRepository;
import com.FSTS.schoolManager.repositories.SalleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmploiDuTempsServiceImpl implements EmploiDuTempsService {

    private final EmploiDuTempsRepository emploiDuTempsRepository;
    private final ModuleRepository moduleRepository;
    private final SalleRepository salleRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;

    @Override
    public EmploiDuTempsDto createEmploiDuTemps(EmploiDuTempsDto dto) {
        log.info("Creating new schedule entry for module: {}", dto.getModule().getNomModule());
        
        validateSchedule(dto);
        
        EmploiDuTemps emploiDuTemps = mapToEntity(dto);
        return mapToDto(emploiDuTempsRepository.save(emploiDuTemps));
    }

    @Override
    public EmploiDuTempsDto updateEmploiDuTemps(Long id, EmploiDuTempsDto dto) {
        log.info("Updating schedule entry with ID: {}", id);
        
        EmploiDuTemps existing = emploiDuTempsRepository.findById(id)
            .orElseThrow(() -> new EmploiDuTempsNotFoundException("Schedule not found with id=" + id));
            
        validateSchedule(dto);
        
        updateEntity(existing, dto);
        return mapToDto(emploiDuTempsRepository.save(existing));
    }

    @Override
    public void deleteEmploiDuTemps(Long id) {
        log.info("Deleting schedule entry with ID: {}", id);
        
        if (!emploiDuTempsRepository.existsById(id)) {
            throw new EmploiDuTempsNotFoundException("Schedule not found with id=" + id);
        }
        
        emploiDuTempsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmploiDuTempsDto> getEmploiDuTempsByModule(Long moduleId) {
        log.info("Fetching schedule for module with ID: {}", moduleId);

        AnneeUniversitaire anneeUniversitaireDb =anneeUniversitaireRepository.findByAnneeCouranteIsTrue().orElseThrow(()->new AnneeUniversitaireNotFoundException("Annee universitaire  not found"));
        
        moduleRepository.findById(moduleId)
            .orElseThrow(() -> new ModuleNotFoundException("Module not found with id=" + moduleId));
            
        return emploiDuTempsRepository.findByModule_IdModuleAndAnneeUniversitaire_AnneeId(moduleId,anneeUniversitaireDb.getAnneeId())
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmploiDuTempsDto> getEmploiDuTempsBySalle(Integer salleId) {
        log.info("Fetching schedule for classroom with ID: {}", salleId);
        
        salleRepository.findById(salleId)
            .orElseThrow(() -> new SalleNotFoundException("Classroom not found with id=" + salleId));

        AnneeUniversitaire anneeUniversitaireDb =anneeUniversitaireRepository
                .findByAnneeCouranteIsTrue().orElseThrow(
                        ()->new AnneeUniversitaireNotFoundException("Annee universitaire  not found"));
            
        return emploiDuTempsRepository.findBySalle_IdAndAnneeUniversitaire_AnneeId(salleId,anneeUniversitaireDb.getAnneeId())
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmploiDuTempsDto> getEmploiDuTempsByJour(DayOfWeek jour) {
        log.info("Fetching schedule for day: {}", jour);

        AnneeUniversitaire anneeUniversitaireDb =anneeUniversitaireRepository
                .findByAnneeCouranteIsTrue()
                .orElseThrow(()->new AnneeUniversitaireNotFoundException("Annee universitaire  not found"));
        return emploiDuTempsRepository.findByJourAndAnneeUniversitaire_AnneeId(jour, anneeUniversitaireDb.getAnneeId())
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    private void validateSchedule(EmploiDuTempsDto dto) {
        // Validate time range
        if (dto.getHeureDebut().isAfter(dto.getHeureFin())) {
            throw new InvalidScheduleException("Start time must be before end time");
        }

        // Check for overlapping schedules in the same room
        List<EmploiDuTemps> overlappingSchedules = emploiDuTempsRepository
            .findBySalleAndJourAndHeureDebutLessThanEqualAndHeureFinGreaterThanEqual(
                dto.getSalle(),
                dto.getJour(),
                dto.getHeureFin(),
                dto.getHeureDebut()
            );

        if (!overlappingSchedules.isEmpty()) {
            throw new InvalidScheduleException("Schedule overlaps with existing schedule in the same room");
        }

        // Validate session type
        if (!isValidSessionType(dto.getTypeSeance())) {
            throw new InvalidScheduleException("Invalid session type: " + dto.getTypeSeance());
        }

        // Validate group number for TD/TP
        if ((dto.getTypeSeance().equals("TD") || dto.getTypeSeance().equals("TP")) 
            && (dto.getGroupe() == null || dto.getGroupe() < 1)) {
            throw new InvalidScheduleException("Group number is required for TD/TP sessions");
        }
    }

    private boolean isValidSessionType(String type) {
        return type != null && (type.equals("CM") || type.equals("TD") || type.equals("TP"));
    }

    private EmploiDuTemps mapToEntity(EmploiDuTempsDto dto) {
        return EmploiDuTemps.builder()
            .id(dto.getId())
            .module(dto.getModule())
            .salle(dto.getSalle())
            .jour(dto.getJour())
            .heureDebut(dto.getHeureDebut())
            .heureFin(dto.getHeureFin())
            .typeCours(EmploiDuTemps.TypeCours.valueOf(dto.getTypeSeance()))
            .groupe(dto.getGroupe())
            .anneeUniversitaire( anneeUniversitaireRepository.findByAnnee(dto.getAnneeUniversitaireNam())
                    .orElseThrow(()->new AnneeUniversitaireNotFoundException("Annee not found")))
            .build();
    }

    private EmploiDuTempsDto mapToDto(EmploiDuTemps emploiDuTemps) {
        return EmploiDuTempsDto.builder()
            .id(emploiDuTemps.getId())
            .module(emploiDuTemps.getModule())
            .salle(emploiDuTemps.getSalle())
            .jour(emploiDuTemps.getJour())
            .heureDebut(emploiDuTemps.getHeureDebut())
            .heureFin(emploiDuTemps.getHeureFin())
            .typeSeance(emploiDuTemps.getTypeCours().toString())
            .groupe(emploiDuTemps.getGroupe())
            .anneeUniversitaireNam(emploiDuTemps.getAnneeUniversitaire().getAnnee())
            .build();
    }

    private void updateEntity(EmploiDuTemps entity, EmploiDuTempsDto dto) {
        entity.setModule(dto.getModule());
        entity.setSalle(dto.getSalle());
        entity.setJour(dto.getJour());
        entity.setHeureDebut(dto.getHeureDebut());
        entity.setHeureFin(dto.getHeureFin());
        entity.setTypeCours(EmploiDuTemps.TypeCours.valueOf(dto.getTypeSeance()));
        entity.setGroupe(dto.getGroupe());
        entity.setAnneeUniversitaire( anneeUniversitaireRepository.findByAnnee(dto.getAnneeUniversitaireNam())
                .orElseThrow(()->new AnneeUniversitaireNotFoundException("Annee not found")));
    }
} 