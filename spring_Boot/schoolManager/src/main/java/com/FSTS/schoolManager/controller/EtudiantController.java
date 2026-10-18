package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.dtos.EtudiantDto;
import com.FSTS.schoolManager.dtos.EtudiantNoteDto;
import com.FSTS.schoolManager.entities.EtudiantPageResponse;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.service.EtudianService;
import com.FSTS.schoolManager.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class EtudiantController {

    private final EtudianService etudiantService;

    public EtudiantController(EtudianService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<EtudiantDto> getEtudiant(@PathVariable Integer userId) {
        return ResponseEntity.ok(etudiantService.getEtudiant(userId));
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @GetMapping("/filiere/{filiereId}")
//    public ResponseEntity<List<EtudiantDto>> getAllFiliereEtudiant(@PathVariable Integer filiereId) {
//        return ResponseEntity.ok(etudiantService.getAllFiliereEtudiant(filiereId));
//    }

    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @PutMapping("/{codeApj}")
    public ResponseEntity<EtudiantDto> updateEtudiant(
            @PathVariable Integer codeApj,
            @RequestParam("etudiant") String etudiantDtoString ,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(etudiantService.updateEtudiant(codeApj, convertEtudiantDto(etudiantDtoString), file));
    }

//    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<EtudiantNoteDto>> getEtudiantsInscritsAuModule(
            @PathVariable Integer moduleId,
            @RequestParam Integer filiereId,
            @RequestParam Module.Semester semester) {
        return ResponseEntity.ok(etudiantService.etudiantsInscrireAuModule(
            moduleId, filiereId, semester));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/filiere/{filiereNom}/semestre/{semester}")
    public ResponseEntity<List<EtudiantDto>> getEtudiantsByFiliereAndSemestre(
            @PathVariable String filiereNom,
            @PathVariable Module.Semester semester) {
        return ResponseEntity.ok(etudiantService.etudiantsByFiliereAndSemestre(filiereNom, semester));
    }


    @PreAuthorize("hasAuthority('ROLE_CHEF_FILIERE')")
    @GetMapping("/filiere/{filiereId}")
    public ResponseEntity<EtudiantPageResponse> getAllEtudiantFiliereWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @PathVariable Integer filiereId
           ) {
        return ResponseEntity.ok(etudiantService.getAllEtudiantFiliereWithPagination(pageNumber,pageSize,filiereId));
    }



    @GetMapping("/{codeApj}/certificate")
    public ResponseEntity<byte[]> generateSchoolCertificate(@PathVariable Integer codeApj) {
        byte[] pdfBytes = etudiantService.generateCertificatScolairePdf(codeApj);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificat_scolarite.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/{userId}/convocation")
    public ResponseEntity<byte[]> generateExamConvocation(
            @PathVariable Integer userId,
            @RequestParam String moduleName,
            @RequestParam String dateExamen,
            @RequestParam String heureExamen,
            @RequestParam String salle) {
        
        byte[] pdfBytes = etudiantService.generateConvocationExamenPdf(
            userId, moduleName, dateExamen, heureExamen, salle);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "convocation_examen.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

     private EtudiantDto convertEtudiantDto (String EtudiantDtoString) throws JsonProcessingException {

         ObjectMapper objectMapper = new ObjectMapper();
         return objectMapper.readValue(EtudiantDtoString, EtudiantDto.class);
     }
} 