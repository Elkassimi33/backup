package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.FiliereDTO;
import com.FSTS.schoolManager.dtos.FiliereResponseDTO;
import com.FSTS.schoolManager.dtos.ModuleDto;
import com.FSTS.schoolManager.entities.Filiere.NiveauFiliere;

import java.util.List;
import java.util.Optional;

public interface FiliereService {
    // CRUD Operations
    FiliereResponseDTO createFiliere(FiliereDTO filiereDTO);
    Optional<FiliereResponseDTO> getFiliereById(Integer id);
    List<FiliereResponseDTO> getAllFilieres();
    FiliereResponseDTO updateFiliere(Integer id, FiliereDTO filiereDTO);
    void deleteFiliere(Integer id);
    
    // Business Operations
    List<FiliereResponseDTO> getFilieresByDepartement(String departementNam);
    List<FiliereResponseDTO> getFilieresByNiveau(NiveauFiliere niveau);
    List<FiliereResponseDTO> getActiveFilieres();
    FiliereResponseDTO assignChefFiliere(Integer filiereId, Integer enseignantId);
    FiliereResponseDTO removeChefFiliere(Integer filiereId);
    FiliereResponseDTO addModule(Integer filiereId, ModuleDto moduleDTO);
    FiliereResponseDTO removeModule(Integer filiereId, Integer moduleId);
    FiliereResponseDTO activateFiliere(Integer id);
    FiliereResponseDTO deactivateFiliere(Integer id);
    List<FiliereDTO> getFiliereByEnseignantId(Integer enseignantId );
} 