package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.*;
import com.FSTS.schoolManager.entities.*;
import com.FSTS.schoolManager.exception.*;
import com.FSTS.schoolManager.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ChefFiliereService interface.
 * This service handles all operations related to department head (Chef Filiere) functionality.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChefFiliereServiceImpl implements ChefFiliereService {

    private final FiliereRepository filiereRepository;
    private final EtudiantRepository etudiantRepository;
    private final ModuleRepository moduleRepository;
    private final NoteRepository noteRepository;
    private final NoteService noteService;
    private final EtudianService etudiantService;
    private final ModuleAffichRepository moduleAffichRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Filiere> getManagedFilieres(Integer enseignantId) {
        log.info("Fetching filieres managed by teacher with ID: {}", enseignantId);
        return filiereRepository.findByChefFiliere_EnseignantId(enseignantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtudiantDto> getFiliereStudents(Integer filiereId) {
        log.info("Fetching students for filiere with ID: {}", filiereId);
        return etudiantService.getAllFiliereEtudiant(filiereId);
    }

    @Override
    @Transactional(readOnly = true)
    public GradeStatistics getModuleGradeStatistics(Long moduleId) {
        log.info("Fetching grade statistics for module with ID: {}", moduleId);
        return noteService.getModuleGradeStatistics(moduleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.FSTS.schoolManager.entities.Module> getFiliereModules(Integer filiereId) {
        log.info("Fetching modules for filiere with ID: {}", filiereId);
        Filiere filiere = filiereRepository.findById(filiereId)
            .orElseThrow(() -> new FiliereNotFoundException("Filiere not found with id=" + filiereId));
        List<com.FSTS.schoolManager.entities.Module> modules = filiere.getModules();
        return modules;
    }

    @Override
    @Transactional(readOnly = true)
    public List<noteDto> getModuleGrades(Long moduleId) {
        log.info("Fetching grades for module with ID: {}", moduleId);
        return noteService.getModuleNotes(moduleId);
    }

    @Override
    public void validateGrade(Integer noteId, boolean validated) {
        log.info("Validating grade with ID: {}, validated: {}", noteId, validated);
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new NoteNotFoundException("Note not found with id=" + noteId));
        
        note.setStatut(validated ? NoteFinal.StatutNote.VALIDE : NoteFinal.StatutNote.NON_VALIDE);
        noteRepository.save(note);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentStatistics> getFiliereStudentStatistics(Integer filiereId) {
        log.info("Fetching student statistics for filiere with ID: {}", filiereId);
        List<Etudiant> etudiants = etudiantRepository.findByFiliereID_FiliereId(filiereId);
        
        return etudiants.stream()
            .map(etudiant -> noteService.getStudentStatistics(etudiant.getCodeApj()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtudiantDto> getFailedStudents(Integer filiereId) {
        log.info("Fetching failed students for filiere with ID: {}", filiereId);
        List<Etudiant> etudiants = etudiantRepository.findByFiliereID_FiliereId(filiereId);
        
        return etudiants.stream()
            .filter(etudiant -> {
                List<NoteFinal> failedNotes = noteService.getStudentFailedModules(etudiant.getCodeApj());
                return !failedNotes.isEmpty();
            })
            .map(etudiant -> etudiantService.getEtudiant(etudiant.getUserId().getId()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtudiantDto> getAtRiskStudents(Integer filiereId) {
        log.info("Fetching at-risk students for filiere with ID: {}", filiereId);
        List<Etudiant> etudiants = etudiantRepository.findByFiliereID_FiliereId(filiereId);
        
        return etudiants.stream()
            .filter(etudiant -> {
                StudentStatistics stats = noteService.getStudentStatistics(etudiant.getCodeApj());
                // Consider a student at risk if they have failed more than 2 modules
                return stats.getFailedModules() > 2;
            })
            .map(etudiant -> etudiantService.getEtudiant(etudiant.getUserId().getId()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleDisplayStatusDto> getModulesWithDisplayStatus(Integer filiereId) {
        log.info("Fetching modules with display status for filiere with ID: {}", filiereId);
        
        Filiere filiere = filiereRepository.findById(filiereId)
            .orElseThrow(() -> new FiliereNotFoundException("Filiere not found with id=" + filiereId));
            
        AnneeUniversitaire currentYear = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));
            
        List<com.FSTS.schoolManager.entities.Module> modules = filiere.getModules();
        List<Long> moduleAffichIds = moduleAffichRepository.ModuleAfficheNormal(currentYear.getAnneeId());
        
        return modules.stream()
            .map(module -> {
                boolean isDisplayed = moduleAffichIds.contains(module.getIdModule());
                return ModuleDisplayStatusDto.builder()
                    .moduleId(module.getIdModule())
                    .moduleName(module.getNomModule())
                    .semester(module.getSemester())
                    .isDisplayed(isDisplayed)
                    .displayNormal(isDisplayed)
                    .displayRattrapage(moduleAffichRepository.ModuleAfficheRatt(currentYear.getAnneeId()).contains(module.getIdModule()))
                    .build();
            })
            .collect(Collectors.toList());
    }
} 