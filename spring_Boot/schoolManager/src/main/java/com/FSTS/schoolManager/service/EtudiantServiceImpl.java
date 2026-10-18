package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.auth.repositories.UserRepository;
import com.FSTS.schoolManager.dtos.EtudiantDto;
import com.FSTS.schoolManager.dtos.EtudiantNoteDto;
import com.FSTS.schoolManager.entities.*;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.exception.AnneeUniversitaireNotFoundException;
import com.FSTS.schoolManager.exception.EtudiantNotFoundException;
import com.FSTS.schoolManager.exception.FiliereNotFoundException;
import com.FSTS.schoolManager.exception.UserNotFoundException;
import com.FSTS.schoolManager.repositories.AnneeUniversitaireRepository;
import com.FSTS.schoolManager.repositories.EtudiantRepository;
import com.FSTS.schoolManager.repositories.FiliereRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.io.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Implementation of the EtudianService interface providing student management functionality.
 * This service handles all operations related to students including CRUD operations,
 * file management, and pagination.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudianService {

    private final EtudiantRepository etudiantRepository;
    private final FileService fileService;
    private final UserRepository userRepository;
    private final FiliereRepository filiereRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;

    @Value("${project.profile}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository, 
                             FileService fileService, 
                             UserRepository userRepository, 
                             FiliereRepository filiereRepository, 
                             AnneeUniversitaireRepository anneeUniversitaireRepository) {
        this.etudiantRepository = etudiantRepository;
        this.fileService = fileService;
        this.userRepository = userRepository;
        this.filiereRepository = filiereRepository;
        this.anneeUniversitaireRepository = anneeUniversitaireRepository;
    }

    @Override
    public EtudiantDto getEtudiant(Integer userId) {
        Etudiant etudiantBD = etudiantRepository.findByUserId_Id(userId).orElseThrow(() -> new EtudiantNotFoundException("Etudiant not found with apj=" + userId));
        String porofileUrl;
        if(etudiantBD.getProfile()!=null)
             porofileUrl = baseUrl+"/file/"+etudiantBD.getProfile();
        else
             porofileUrl=null;

//        7. map to Etudiant and return it

        return new EtudiantDto(
                etudiantBD.getCodeApj(),
                etudiantBD.getUserId().getId(),
                etudiantBD.getNom(),
                etudiantBD.getPrenom(),
                etudiantBD.getCne(),
                etudiantBD.getDateNaissance(),
                etudiantBD.getAdresse(),
                etudiantBD.getPay(),
                etudiantBD.getTelephone(),
                etudiantBD.getAnneeActuelle(),
                etudiantBD.getProfile(),
                porofileUrl,
                etudiantBD.getDateInscription(),
                etudiantBD.getSemester(),
                etudiantBD.getAnneeUniversitaire().getAnnee(),
                etudiantBD.getStatut(),
                etudiantBD.getFiliereID().getFiliereId()
        );

    }

    @Override
    public List<EtudiantDto> getAllFiliereEtudiant(Integer filiereId) {

        List<Etudiant> etudiants = etudiantRepository.findByFiliereID_FiliereId(filiereId);

        return etudiants.stream().map((etudiant)->{
            //        generate profilerUrl
            String porofileUrl = baseUrl+"/file/"+etudiant.getProfile();

            return new EtudiantDto(
                    etudiant.getCodeApj(),
                    etudiant.getUserId().getId(),
                    etudiant.getNom(),
                    etudiant.getPrenom(),
                    etudiant.getCne(),
                    etudiant.getDateNaissance(),
                    etudiant.getAdresse(),
                    etudiant.getPay(),
                    etudiant.getTelephone(),
                    etudiant.getAnneeActuelle(),
                    etudiant.getProfile(),
                    porofileUrl,
                    etudiant.getDateInscription(),
                    etudiant.getSemester(),
                    etudiant.getAnneeUniversitaire().getAnnee(),
                    etudiant.getStatut(),
                    etudiant.getFiliereID().getFiliereId()
            );
        }).toList();
    }

    @Override
    public EtudiantDto updateEtudiant(Integer codeApj, EtudiantDto etudiantDto, MultipartFile file) throws IOException {
//        1. check if the etudiant exist with the given id

            Etudiant etudiantBD = etudiantRepository.findById(codeApj).orElseThrow(()-> new EtudiantNotFoundException("Etudiant not found with apj="+codeApj));

//        2. if fil is null,do nothing
//           if file is not null,the deleted existing file associated with the record
//           and upload the new file
            String fileName=etudiantBD.getProfile();
            if(file != null){
                Files.deleteIfExists(Paths.get(path+ File.separator + fileName));
                fileName = fileService.uploadFile(path,file,codeApj.toString());
            }


//         3. set the etudiantDto's profile, accorded to step2
        etudiantDto.setProfile(fileName);


//          4. map it to Etudianr object
            Etudiant etudiant =new Etudiant(
                    etudiantDto.getCodeApj(),
                    userRepository.findById(etudiantBD.getUserId()).orElseThrow(()->new UserNotFoundException("Etudiant not found with id="+etudiantBD.getUserId())),
                    etudiantDto.getNom(),
                    etudiantDto.getPrenom(),
                    etudiantDto.getCne(),
                    etudiantDto.getDateNaissance(),
                    etudiantDto.getAdresse(),
                    etudiantDto.getPay(),
                    etudiantDto.getTelephone(),
                    etudiantDto.getAnneeActuelleId(),
                    etudiantDto.getProfile(),
                    etudiantDto.getDateInscription(),
                    etudiantDto.getSemester(),
                    anneeUniversitaireRepository.findByAnnee(etudiantDto.getAnneeUniversitaire())
                            .orElseThrow(()->new AnneeUniversitaireNotFoundException("Annee not found")),
                    etudiantDto.getStatut(),
                    filiereRepository.findById(etudiantDto.getFilierId()).orElseThrow(()->new FiliereNotFoundException("Filiere not found with id="+etudiantDto.getFilierId()))
            );

//        5.save Etudiant object-> return saved Etudiant object
            Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        return mapToEtudiantDto(savedEtudiant);
    }

   @Override
public EtudiantPageResponse getAllEtudiantFiliereWithPagination(Integer pageNumber, Integer pageSize, Integer filiereId) {
    // Validate input parameters
    if (pageNumber < 0) {
        throw new IllegalArgumentException("Page number cannot be negative");
    }
    if (pageSize <= 0) {
        throw new IllegalArgumentException("Page size must be greater than zero");
    }
    if (filiereId == null) {
        throw new IllegalArgumentException("Filiere ID cannot be null");
    }

    // Verify filiere exists
    if (!filiereRepository.existsById(filiereId)) {
        throw new FiliereNotFoundException("Filiere not found with id: " + filiereId);
    }

    // Create pageable object
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    
    // Get paginated students
    Page<Etudiant> etudiantPages = etudiantRepository.findByFiliereID_FiliereId(filiereId, pageable);

    // Map to DTOs using the helper method
    List<EtudiantDto> etudiantsDtoList = etudiantPages.getContent()
        .stream()
        .map(this::mapToEtudiantDto)
        .toList();

    // Create and return response
    return new EtudiantPageResponse(
        etudiantsDtoList,
        pageNumber,
        pageSize,
        etudiantPages.getTotalElements(),
        etudiantPages.getTotalPages(),
        etudiantPages.isLast()
    );
}

// Helper method (as in previous message)
private EtudiantDto mapToEtudiantDto(Etudiant etudiant) {
    String profileUrl = etudiant.getProfile() != null ? baseUrl + "/file/" + etudiant.getProfile() : null;
    return new EtudiantDto(
        etudiant.getCodeApj(),
        etudiant.getUserId().getId(),
        etudiant.getNom(),
        etudiant.getPrenom(),
        etudiant.getCne(),
        etudiant.getDateNaissance(),
        etudiant.getAdresse(),
        etudiant.getPay(),
        etudiant.getTelephone(),
        etudiant.getAnneeActuelle(),
        etudiant.getProfile(),
        profileUrl,
        etudiant.getDateInscription(),
        etudiant.getSemester(),
        etudiant.getAnneeUniversitaire().getAnnee(),
        etudiant.getStatut(),
        etudiant.getFiliereID().getFiliereId()
    );
}

    @Override
    public EtudiantPageResponse getAllEtudiantFiliereWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir,Integer filiereId) {
        Sort sort = dir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Etudiant> etudiantPages = etudiantRepository.findByFiliereID_FiliereId(filiereId, pageable);
        List<Etudiant> etudiants=etudiantPages.getContent();

        List<EtudiantDto> etudiantDtoList  = etudiants.stream().map(this::mapToEtudiantDto).toList();
        return new EtudiantPageResponse(etudiantDtoList,pageNumber,pageSize, (int) etudiantPages.getTotalElements(),etudiantPages.getTotalPages(),etudiantPages.isLast());
    }

    @Override
     public List<EtudiantNoteDto> etudiantsInscrireAuModule(Integer moduleID, Integer filiereId, Module.Semester semester){

        AnneeUniversitaire anneeUniversitaireDB=anneeUniversitaireRepository.findByAnneeCouranteIsTrue().orElseThrow(()->new AnneeUniversitaireNotFoundException("Annee universitaire  not found"));
        List<Etudiant> etudiantsNormal= etudiantRepository.findByFiliereID_FiliereIdAndStatutAndAnneeUniversitaire_AnneeIdAndSemester(filiereId, Etudiant.StatutEtudiant.NORMAL, anneeUniversitaireDB.getAnneeId(), semester);
        List<Etudiant> etudiantsREDOUBLAN= etudiantRepository.findByFiliereID_FiliereIdAndStatutAndAnneeUniversitaire_AnneeIdAndSemester(filiereId, Etudiant.StatutEtudiant.REDOUBLANT, anneeUniversitaireDB.getAnneeId(), semester);
         List<Etudiant> etudiantsREDOUBLANT_PARTIELetRESERVISTE=etudiantRepository.
                 findReserveOrPartialRepeatingStudentsInFiliereAndModule(filiereId, moduleID);

         etudiantsNormal.addAll(etudiantsREDOUBLAN);
         etudiantsNormal.addAll(etudiantsREDOUBLANT_PARTIELetRESERVISTE);

         return etudiantsNormal.stream().map(e->
             new EtudiantNoteDto (
                    e.getCodeApj(),
                    e.getNom(),
                    e.getPrenom(),
                    null,
                    null
            )
         ).toList();
     }

    @Override
    public List<EtudiantDto> etudiantsByFiliereAndSemestre(String filiereNom, Module.Semester semester) {

        Filiere filiere =filiereRepository.findByNom(filiereNom).orElseThrow(()->new FiliereNotFoundException("Filiere not found with the given name: "+filiereNom));
        Integer filiereId=filiere.getFiliereId();
        AnneeUniversitaire anneeUniversitaireDB=anneeUniversitaireRepository.findByAnneeCouranteIsTrue().orElseThrow(()->new AnneeUniversitaireNotFoundException("Annee universitaire  not found"));

        List<Etudiant> EtudiantsDB= etudiantRepository.findByFiliereID_FiliereIdAndAnneeUniversitaire_AnneeIdAndSemester(filiereId,anneeUniversitaireDB.getAnneeId(),semester);

        return EtudiantsDB.stream().map(this::mapToEtudiantDto).toList();
    }

    @Override
public byte[] generateCertificatScolairePdf(Integer codeApj) {
    try {
        // 1. Fetch student details using userId
        Etudiant etudiant = etudiantRepository.findById(codeApj)
            .orElseThrow(() -> new EtudiantNotFoundException("Etudiant not found with codeApj=" + codeApj));

        // 2. Create PDF document
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // 3. Define fonts for better styling
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

            // 4. Add university header
            Paragraph universityName = new Paragraph("UNIVERSITÉ HASSAN 1ER", headerFont);
            universityName.setAlignment(Element.ALIGN_CENTER);
            document.add(universityName);
            
            Paragraph facultyName = new Paragraph("FACULTÉ DES SCIENCES ET TECHNIQUES DE SETTAT", headerFont);
            facultyName.setAlignment(Element.ALIGN_CENTER);
            document.add(facultyName);
            
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // 5. Add certificate title
            Paragraph title = new Paragraph("CERTIFICAT DE SCOLARITÉ", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // 6. Add certificate content
            Paragraph introduction = new Paragraph("Le Doyen de la Faculté des Sciences et Techniques de Settat atteste que l'étudiant(e) :", regularFont);
            document.add(introduction);
            
            document.add(Chunk.NEWLINE);

            // 7. Add student information with proper formatting
            document.add(new Paragraph(createBoldLabel("Nom et Prénom : ", boldFont) + 
                formatStudentName(etudiant.getNom(), etudiant.getPrenom()), regularFont));
            
            if (etudiant.getCne() != null && !etudiant.getCne().trim().isEmpty()) {
                document.add(new Paragraph(createBoldLabel("Numéro de la carte d'identité nationale : ", boldFont) + 
                    etudiant.getCne(), regularFont));
            }
            
            document.add(new Paragraph(createBoldLabel("Code étudiant : ", boldFont) + 
                etudiant.getCodeApj(), regularFont));
            
            if (etudiant.getDateNaissance() != null) {
                document.add(new Paragraph(createBoldLabel("Date de naissance : ", boldFont) + 
                    formatDate(etudiant.getDateNaissance()), regularFont));
            }
            
            document.add(Chunk.NEWLINE);

            // 8. Add academic information
            String academicInfo = buildAcademicInfo(etudiant);
            document.add(new Paragraph(academicInfo, regularFont));
            
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // 9. Add certificate purpose
            document.add(new Paragraph("En foi de quoi, le présent certificat lui est délivré pour servir et valoir ce que de droit.", regularFont));
            
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // 10. Add date and signature
            String currentDate = java.time.LocalDate.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );
            
            Paragraph datePlace = new Paragraph("Fait à Settat, le " + currentDate, regularFont);
            datePlace.setAlignment(Element.ALIGN_RIGHT);
            document.add(datePlace);
            
            document.add(Chunk.NEWLINE);
            
            Paragraph signature = new Paragraph("Le Doyen", boldFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        return baos.toByteArray();
        
    } catch (EtudiantNotFoundException e) {
        throw e; // Re-throw business exceptions
    } catch (DocumentException e) {
        throw new RuntimeException("Error generating PDF certificate: " + e.getMessage(), e);
    } catch (Exception e) {
        throw new RuntimeException("Unexpected error while generating certificate: " + e.getMessage(), e);
    }
}

/**
 * Helper method to create bold labels for PDF content
 */
private String createBoldLabel(String label, Font boldFont) {
    return label;
}

/**
 * Helper method to format student name properly
 */
private String formatStudentName(String nom, String prenom) {
    StringBuilder name = new StringBuilder();
    
    if (nom != null && !nom.trim().isEmpty()) {
        name.append(nom.trim().toUpperCase());
    }
    
    if (prenom != null && !prenom.trim().isEmpty()) {
        if (name.length() > 0) {
            name.append(" ");
        }
        // Capitalize first letter of each word in prenom
        name.append(capitalizeWords(prenom.trim()));
    }
    
    return name.toString();
}

/**
 * Helper method to capitalize words
 */
private String capitalizeWords(String input) {
    if (input == null || input.trim().isEmpty()) {
        return input;
    }
    
    String[] words = input.toLowerCase().split("\\s+");
    StringBuilder result = new StringBuilder();
    
    for (int i = 0; i < words.length; i++) {
        if (i > 0) {
            result.append(" ");
        }
        if (words[i].length() > 0) {
            result.append(Character.toUpperCase(words[i].charAt(0)));
            if (words[i].length() > 1) {
                result.append(words[i].substring(1));
            }
        }
    }
    
    return result.toString();
}

/**
 * Helper method to format dates consistently
 */
private String formatDate(java.time.LocalDate date) {
    if (date == null) {
        return "N/A";
    }
    return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
}

/**
 * Helper method to build academic information string
 */
private String buildAcademicInfo(Etudiant etudiant) {
    StringBuilder academicInfo = new StringBuilder();
    academicInfo.append("Poursuit régulièrement ses études à la Faculté des Sciences et Techniques de Settat");
    
    // Add academic year
    if (etudiant.getAnneeUniversitaire() != null) {
        academicInfo.append(" pour l'année universitaire ");
        // Assuming AnneeUniversitaire has a method to get the year string
        // You might need to adjust this based on your actual AnneeUniversitaire entity
        academicInfo.append(getAcademicYearString(etudiant.getAnneeUniversitaire()));
    }
    
    // Add semester and filiere information
    if (etudiant.getSemester() != null || etudiant.getFiliereID() != null) {
        academicInfo.append(" en ");
        
        if (etudiant.getSemester() != null) {
            academicInfo.append(formatSemester(etudiant.getSemester()));
        }
        
        if (etudiant.getFiliereID() != null && etudiant.getFiliereID().getNom() != null) {
            if (etudiant.getSemester() != null) {
                academicInfo.append(" ");
            }
            academicInfo.append(etudiant.getFiliereID().getNom());
        }
    }
    
    academicInfo.append(".");
    return academicInfo.toString();
}

/**
 * Helper method to get academic year string from AnneeUniversitaire entity
 */
private String getAcademicYearString(AnneeUniversitaire anneeUniversitaire) {
    // You'll need to implement this based on your AnneeUniversitaire entity structure
    // This is a placeholder - adjust according to your entity's actual methods
    if (anneeUniversitaire.getAnnee() != null) {
        return anneeUniversitaire.getAnnee();
    }
    // If you have separate start and end year fields, you might do something like:
    // return anneeUniversitaire.getAnneeDebut() + "/" + anneeUniversitaire.getAnneeFin();
    return "N/A";
}

/**
 * Helper method to format semester information
 */
private String formatSemester(Module.Semester semester) {
    if (semester == null) {
        return "";
    }
    
    // Format semester enum to readable format
    // You might need to adjust this based on your actual Semester enum values
    switch (semester) {
        case S1:
            return "S1";
        case S2:
            return "S2";
        case S3:
            return "S3";
        case S4:
            return "S4";
        case S5:
            return "S5";
        case S6:
            return "S6";
        default:
            return semester.toString();
    }
}

@Override
public byte[] generateConvocationExamenPdf(Integer userId, String moduleName, String dateExamen, String heureExamen, String salle) {
    Etudiant etudiant = etudiantRepository.findByUserId_Id(userId)
        .orElseThrow(() -> new EtudiantNotFoundException("Student not found with user id=" + userId));

    try {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Add header
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph header = new Paragraph("CONVOCATION AUX EXAMENS", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(20);
        document.add(header);

        // Add student information
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph studentInfo = new Paragraph();
        studentInfo.setFont(normalFont);
        studentInfo.add("Nom et Prénom: " + etudiant.getNom() + " " + etudiant.getPrenom() + "\n");
        studentInfo.add("CNE: " + etudiant.getCne() + "\n");
        studentInfo.add("Filière: " + etudiant.getFiliereID().getNom() + "\n");
        studentInfo.add("Semestre: " + etudiant.getSemester() + "\n\n");
        document.add(studentInfo);

        // Add exam information
        Paragraph examInfo = new Paragraph();
        examInfo.setFont(normalFont);
        examInfo.add("Module: " + moduleName + "\n");
        examInfo.add("Date: " + dateExamen + "\n");
        examInfo.add("Heure: " + heureExamen + "\n");
        examInfo.add("Salle: " + salle + "\n\n");
        document.add(examInfo);

        // Add footer
        Paragraph footer = new Paragraph();
        footer.setFont(normalFont);
        footer.add("Signature du Responsable\n");
        footer.add("Faculté des Sciences et Techniques de Settat");
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return out.toByteArray();
    } catch (DocumentException e) {
        throw new RuntimeException("Error generating exam convocation PDF", e);
    }
}
}
