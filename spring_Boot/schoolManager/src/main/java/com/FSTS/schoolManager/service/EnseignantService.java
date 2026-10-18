package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.EnseignantDto;
import com.FSTS.schoolManager.entities.Enseignant;
import com.FSTS.schoolManager.entities.Module;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;

public interface EnseignantService {

    EnseignantDto getEnseignantByUserId(Integer userId);

    EnseignantDto updateEnseignant(Integer enseignantId, EnseignantDto enseignantDto, MultipartFile file) throws IOException;

    List<Module> modulesTaughtThisYear(Integer enseignantId);

    List<EnseignantDto> getDepartementEnseignant(String departementNam);

    List<EnseignantDto> findByStatut(Enseignant.StatutEnseignant statut);

    List<EnseignantDto> findBySpecialiteContainingIgnoreCase(String specialite);

    List<EnseignantDto> getAllEnseignants();
}
