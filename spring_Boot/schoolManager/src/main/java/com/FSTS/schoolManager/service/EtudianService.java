package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.EtudiantDto;
import com.FSTS.schoolManager.dtos.EtudiantNoteDto;
import com.FSTS.schoolManager.entities.Etudiant;
import com.FSTS.schoolManager.entities.EtudiantPageResponse;
import com.FSTS.schoolManager.entities.Module;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EtudianService  {

        EtudiantDto getEtudiant(Integer userId);

        List<EtudiantDto> getAllFiliereEtudiant(Integer filiereId);

        EtudiantDto updateEtudiant(Integer EtudiantId, EtudiantDto etudiantDto, MultipartFile file) throws IOException ;

        EtudiantPageResponse getAllEtudiantFiliereWithPagination(Integer pageNumber, Integer pageSize,Integer filiereId);

        EtudiantPageResponse getAllEtudiantFiliereWithPaginationAndSorting(Integer pageNumber,Integer pageSize,String sortBy,String dir,Integer filiereId);

        List<EtudiantNoteDto> etudiantsInscrireAuModule(Integer moduleID, Integer filiereId, Module.Semester semester);

        List<EtudiantDto> etudiantsByFiliereAndSemestre(String filiereNom, Module.Semester semester);

        byte[] generateCertificatScolairePdf(Integer userId);

        byte[] generateConvocationExamenPdf(Integer userId, String moduleName, String dateExamen, String heureExamen, String salle);
}
