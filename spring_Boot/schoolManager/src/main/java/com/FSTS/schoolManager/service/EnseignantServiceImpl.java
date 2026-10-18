package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.auth.repositories.UserRepository;
import com.FSTS.schoolManager.dtos.EnseignantDto;
import com.FSTS.schoolManager.dtos.EtudiantDto;
import com.FSTS.schoolManager.entities.*;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.exception.*;
import com.FSTS.schoolManager.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final FileService fileService;
    private final DepartementRepository departementRepository;
    private final UserRepository userRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;
    private final ModuleRepository moduleRepository;
    private final FiliereRepository filiereRepository;

    @Value("${project.profile}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;


    public EnseignantServiceImpl(EnseignantRepository enseignantRepository, FileService fileService, DepartementRepository departementRepository, UserRepository userRepository, AnneeUniversitaireRepository anneeUniversitaireRepository, ModuleRepository moduleRepository, FiliereRepository filiereRepository) {
        this.enseignantRepository = enseignantRepository;
        this.fileService = fileService;

        this.departementRepository = departementRepository;
        this.userRepository = userRepository;
        this.anneeUniversitaireRepository = anneeUniversitaireRepository;
        this.moduleRepository = moduleRepository;
        this.filiereRepository = filiereRepository;
    }

    @Override
    public EnseignantDto getEnseignantByUserId(Integer userId) {
        Enseignant enseignantBD = enseignantRepository.findByUser_Id(userId).orElseThrow(()-> new EnseignantNotFoundException("Enseignant not found with user id="+userId));

        return mapToEnseignantDto(enseignantBD);
    }

    @Override
    public EnseignantDto updateEnseignant(Integer enseignantId, EnseignantDto enseignantDto, MultipartFile file) throws IOException {
        //        1. check if the enseignant exist with the given id

        Enseignant enseignantBD = enseignantRepository.findById(enseignantId).orElseThrow(()-> new EtudiantNotFoundException("Enseignant not found with apj="+enseignantId));

//        2. if fil is null,do nothing
//           if file is not null,the deleted existing file associated with the record
//           and upload the new file
        String fileName=enseignantBD.getProfil();
        if(file != null){
            Files.deleteIfExists(Paths.get(path+ File.separator + fileName));
            fileName = fileService.uploadFile(path,file,enseignantId.toString());
        }


//         3. set the EnseignantDto's profile, accorded to step2
        enseignantDto.setProfil(fileName);


//          4. map it to Etudianr object
        Enseignant enseignant =new Enseignant(
                enseignantBD.getEnseignantId(),
                userRepository.findById(enseignantDto.getUserId()).orElseThrow(()->new UserNotFoundException("Enseignant not found with id="+enseignantDto.getUserId())),
                enseignantDto.getNom(),
                enseignantDto.getPrenom(),
                enseignantDto.getProfil(),
                enseignantDto.getSpecialite(),
                enseignantDto.getGrade(),
                enseignantDto.getTelephone(),
                departementRepository.findByNom(enseignantDto.getDepartementNam()).orElseThrow(()->new DepartementNotFoundException("Departement not found with nam="+enseignantDto.getDepartementNam())),
                enseignantBD.getStatut(),
                enseignantBD.getCreatedAt(),
                LocalDateTime.now()

        );

//        5.save Enseignant object-> return saved Enseignant object
        Enseignant savedEnseignant = enseignantRepository.save(enseignant);

        return mapToEnseignantDto(savedEnseignant);

    }

    @Override
    public List<Module> modulesTaughtThisYear(Integer enseignantId) {
//        get annee Universitaire actual id
        AnneeUniversitaire anneeUniversitaireDB=anneeUniversitaireRepository.findByAnneeCouranteIsTrue().orElseThrow(()->new AnneeUniversitaireNotFoundException("Annee universitaire  not found"));

        return moduleRepository.findByAnneeUniversitaire_AnneeIdAndEnseignant_EnseignantId(anneeUniversitaireDB.getAnneeId(),enseignantId);

    }

    @Override
    public List<EnseignantDto> getDepartementEnseignant(String departementNam) {
        Departement departement= departementRepository.findByNom(departementNam).orElseThrow(()->new DepartementNotFoundException("Departement not found with nam="+departementNam));
        List<Enseignant> departementEnseignant =enseignantRepository.findByDepartement_DepartementId(departement.getDepartementId());

        return departementEnseignant.stream().map(this::mapToEnseignantDto).toList();
    }

    @Override
    public List<EnseignantDto> findByStatut(Enseignant.StatutEnseignant statut) {

        List<Enseignant> enseignantParStatut =enseignantRepository.findByStatut(statut);

        return enseignantParStatut.stream().map(this::mapToEnseignantDto).toList();
    }

    @Override
    public List<EnseignantDto> findBySpecialiteContainingIgnoreCase(String specialite) {
        List<Enseignant> enseignantParStatut =enseignantRepository.findBySpecialiteContainingIgnoreCase(specialite);

        return enseignantParStatut.stream().map(this::mapToEnseignantDto).toList();
    }

    @Override
    public List<EnseignantDto> getAllEnseignants() {
        return enseignantRepository.findAll()
                .stream().map(this::mapToEnseignantDto).toList();
    }

    public List<Filiere> filiereDerige(Integer enseingantId){
        return  filiereRepository.findByChefFiliere_EnseignantId(enseingantId);
    }

    private EnseignantDto mapToEnseignantDto(Enseignant enseignant) {
        String profileUrl = enseignant.getProfil() != null ? baseUrl + "/file/" + enseignant.getProfil() : null;
        return  new EnseignantDto(
                enseignant.getEnseignantId(),
                enseignant.getUser().getId(),
                enseignant.getNom(),
                enseignant.getPrenom(),
                enseignant.getProfil(),
                profileUrl,
                enseignant.getSpecialite(),
                enseignant.getGrade(),
                enseignant.getTelephone(),
                enseignant.getDepartement().getNom(),
                enseignant.getStatut()
        );
    }
}
