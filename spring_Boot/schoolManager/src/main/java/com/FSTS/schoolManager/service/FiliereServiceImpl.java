package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.FiliereDTO;
import com.FSTS.schoolManager.dtos.FiliereResponseDTO;
import com.FSTS.schoolManager.dtos.DepartementDto;
import com.FSTS.schoolManager.dtos.EnseignantDto;
import com.FSTS.schoolManager.dtos.ModuleDto;
import com.FSTS.schoolManager.entities.Departement;
import com.FSTS.schoolManager.entities.Enseignant;
import com.FSTS.schoolManager.entities.Filiere;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.exception.DepartementNotFoundException;
import com.FSTS.schoolManager.exception.EnseignantNotFoundException;
import com.FSTS.schoolManager.repositories.FiliereRepository;
import com.FSTS.schoolManager.repositories.DepartementRepository;
import com.FSTS.schoolManager.repositories.EnseignantRepository;
import com.FSTS.schoolManager.repositories.ModuleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FiliereServiceImpl implements FiliereService {

    private final FiliereRepository filiereRepository;
    private final DepartementRepository departementRepository;
    private final EnseignantRepository enseignantRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public FiliereResponseDTO createFiliere(FiliereDTO filiereDTO) {
        Filiere filiere = new Filiere();
        filiere.setNom(filiereDTO.getNom());
        filiere.setDescription(filiereDTO.getDescription());
        filiere.setNiveau(filiereDTO.getNiveau());
        filiere.setDureeEtudes(filiereDTO.getDureeEtudes());
        filiere.setActive(filiereDTO.getActive());
        filiere.setCreatedAt(LocalDateTime.now());

        if(filiereDTO.getChefFiliereId() != null){
            Enseignant enseignant =enseignantRepository.findById(filiereDTO.getChefFiliereId())
                    .orElseThrow(()->new EnseignantNotFoundException("Enseignant not fount with id="+filiereDTO.getChefFiliereId()));
            filiere.setChefFiliere(enseignant);
        }

        if (filiereDTO.getDepartementNam() != null) {
            Departement departement = departementRepository.findByNom(filiereDTO.getDepartementNam())
                    .orElseThrow(() -> new EntityNotFoundException("Departement not found"));
            filiere.setDepartement(departement);
        }

        Filiere savedFiliere = filiereRepository.save(filiere);
        return mapToResponseDTO(savedFiliere);
    }

    @Override
    public Optional<FiliereResponseDTO> getFiliereById(Integer id) {
        return filiereRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    @Override
    public List<FiliereResponseDTO> getAllFilieres() {
        return filiereRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FiliereResponseDTO updateFiliere(Integer id, FiliereDTO filiereDTO) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filiere not found with id: " + id));

        filiere.setNom(filiereDTO.getNom());
        filiere.setDescription(filiereDTO.getDescription());
        filiere.setNiveau(filiereDTO.getNiveau());
        filiere.setDureeEtudes(filiereDTO.getDureeEtudes());
        filiere.setActive(filiereDTO.getActive());
        filiere.setUpdatedAt(LocalDateTime.now());

        if(filiereDTO.getChefFiliereId() != null){
            Enseignant enseignant =enseignantRepository.findById(filiereDTO.getChefFiliereId())
                    .orElseThrow(()->new EnseignantNotFoundException("Enseignant not fount with id="+filiereDTO.getChefFiliereId()));
            filiere.setChefFiliere(enseignant);
        }

        if (filiereDTO.getDepartementNam() != null) {
            Departement departement = departementRepository.findByNom(filiereDTO.getDepartementNam())
                    .orElseThrow(() -> new EntityNotFoundException("Departement not found"));
            filiere.setDepartement(departement);
        }

        Filiere savedFiliere = filiereRepository.save(filiere);
        return mapToResponseDTO(savedFiliere);
    }

    @Override
    public void deleteFiliere(Integer id) {
        filiereRepository.deleteById(id);
    }

    @Override
    public List<FiliereResponseDTO> getFilieresByDepartement(String departementNam) {

        Departement departement =departementRepository.findByNom(departementNam)
                .orElseThrow(()->new DepartementNotFoundException("departement not found with nam="+departementNam));
        return filiereRepository.findByDepartement_DepartementId(departement.getDepartementId()).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiliereResponseDTO> getFilieresByNiveau(Filiere.NiveauFiliere niveau) {
        return filiereRepository.findByNiveau(niveau).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiliereResponseDTO> getActiveFilieres() {
        return filiereRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FiliereResponseDTO assignChefFiliere(Integer filiereId, Integer enseignantId) {
        Filiere filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new EntityNotFoundException("Filiere not found with id: " + filiereId));

        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new EntityNotFoundException("Enseignant not found with id: " + enseignantId));

        filiere.setChefFiliere(enseignant);
        Filiere updatedFiliere = filiereRepository.save(filiere);
        return mapToResponseDTO(updatedFiliere);
    }

    @Override
    public FiliereResponseDTO removeChefFiliere(Integer filiereId) {
        Filiere filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new EntityNotFoundException("Filiere not found with id: " + filiereId));

        filiere.setChefFiliere(null);
        Filiere updatedFiliere = filiereRepository.save(filiere);
        return mapToResponseDTO(updatedFiliere);
    }

    @Override
    public FiliereResponseDTO addModule(Integer filiereId, ModuleDto moduleDTO) {
        Filiere filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new EntityNotFoundException("Filiere not found with id: " + filiereId));

        Module module = mapModuleDTOToEntity(moduleDTO);
        module.setFiliere(filiere);
        Module savedModule = moduleRepository.save(module);

        filiere.getModules().add(savedModule);
        Filiere updatedFiliere = filiereRepository.save(filiere);
        return mapToResponseDTO(updatedFiliere);
    }

    @Override
    public FiliereResponseDTO removeModule(Integer filiereId, Integer moduleId) {
        Filiere filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new EntityNotFoundException("Filiere not found with id: " + filiereId));

        Module module = moduleRepository.findById(moduleId.longValue())
                .orElseThrow(() -> new EntityNotFoundException("Module not found with id: " + moduleId));

        filiere.getModules().remove(module);
        moduleRepository.delete(module);
        Filiere updatedFiliere = filiereRepository.save(filiere);
        return mapToResponseDTO(updatedFiliere);
    }

    @Override
    public FiliereResponseDTO activateFiliere(Integer id) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filiere not found with id: " + id));

        filiere.setActive(true);
        Filiere updatedFiliere = filiereRepository.save(filiere);
        return mapToResponseDTO(updatedFiliere);
    }

    @Override
    public FiliereResponseDTO deactivateFiliere(Integer id) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filiere not found with id: " + id));

        filiere.setActive(false);
        Filiere updatedFiliere = filiereRepository.save(filiere);
        return mapToResponseDTO(updatedFiliere);
    }

    private FiliereResponseDTO mapToResponseDTO(Filiere filiere) {
        FiliereResponseDTO responseDTO = new FiliereResponseDTO();
        responseDTO.setFiliereId(filiere.getFiliereId());
        responseDTO.setNom(filiere.getNom());
        responseDTO.setDescription(filiere.getDescription());
        responseDTO.setNiveau(filiere.getNiveau());
        responseDTO.setActive(filiere.getActive());
        responseDTO.setNombreModules(filiere.getNombreModules());
        responseDTO.setDureeEtudes(filiere.getDureeEtudes());

        if (filiere.getDepartement() != null) {
            responseDTO.setDepartement(mapDepartementToDTO(filiere.getDepartement()));
        }

        if (filiere.getChefFiliere() != null) {
            responseDTO.setChefFiliere(mapEnseignantToDTO(filiere.getChefFiliere()));
        }

        responseDTO.setModules(filiere.getModules().stream()
                .map(this::mapModuleToDTO)
                .collect(Collectors.toList()));

        return responseDTO;
    }

    private DepartementDto mapDepartementToDTO(Departement departement) {
        return DepartementDto.builder()
                .departementId(departement.getDepartementId())
                .nom(departement.getNom())
                .description(departement.getDescription())
                .build();
    }

    private EnseignantDto mapEnseignantToDTO(Enseignant enseignant) {
        return new EnseignantDto(
            enseignant.getEnseignantId(),
            enseignant.getUser() != null ? enseignant.getUser().getId() : null,
            enseignant.getNom(),
            enseignant.getPrenom(),
            enseignant.getProfil(),
            null,
            enseignant.getSpecialite(),
            enseignant.getGrade(),
            enseignant.getTelephone(),
            enseignant.getDepartement() != null ? enseignant.getDepartement().getNom() : null,
            enseignant.getStatut()
        );
    }

    private ModuleDto mapModuleToDTO(Module module) {
        return ModuleDto.builder()
                .idModule(module.getIdModule())
                .nomModule(module.getNomModule())
                .codeModule(module.getCodeModule())
                .volumeHoraire(module.getVolumeHoraire())
                .filiere(module.getFiliere().getFiliereId())
                .build();
    }

    private Module mapModuleDTOToEntity(ModuleDto dto) {
        return Module.builder()
                .nomModule(dto.getNomModule())
                .codeModule(dto.getCodeModule())
                .volumeHoraire(dto.getVolumeHoraire())
                .build();
    }

    public List<FiliereDTO> getFiliereByEnseignantId(Integer enseignantId ){
        return filiereRepository.findByChefFiliere_EnseignantId(enseignantId)
                .stream().map((f)->new FiliereDTO(
                        f.getFiliereId(),
                        f.getNom(),
                        f.getDescription(),
                        f.getDureeEtudes(),
                        f.getNiveau(),
                        f.getDepartement().getNom(),
                        f.getChefFiliere().getEnseignantId(),
                        f.getActive()
                )).toList();
    }
} 