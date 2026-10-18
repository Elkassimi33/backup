package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.ModuleDto;
import com.FSTS.schoolManager.entities.AnneeUniversitaire;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.exception.AnneeUniversitaireNotFoundException;
import com.FSTS.schoolManager.exception.DepartementNotFoundException;
import com.FSTS.schoolManager.exception.EnseignantNotFoundException;
import com.FSTS.schoolManager.exception.FiliereNotFoundException;
import com.FSTS.schoolManager.exception.ModuleNotFoundException;
import com.FSTS.schoolManager.repositories.AnneeUniversitaireRepository;
import com.FSTS.schoolManager.repositories.DepartementRepository;
import com.FSTS.schoolManager.repositories.EnseignantRepository;
import com.FSTS.schoolManager.repositories.FiliereRepository;
import com.FSTS.schoolManager.repositories.ModuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ModuleSeviceImpl implements ModuleSevice {

    private final ModuleRepository moduleRepository;
    private final FiliereRepository filiereRepository;
    private final EnseignantRepository enseignantRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;
    private final DepartementRepository departementRepository;

    public ModuleSeviceImpl(ModuleRepository moduleRepository, FiliereRepository filiereRepository, 
            EnseignantRepository enseignantRepository, AnneeUniversitaireRepository anneeUniversitaireRepository,
            DepartementRepository departementRepository) {
        this.moduleRepository = moduleRepository;
        this.filiereRepository = filiereRepository;
        this.enseignantRepository = enseignantRepository;
        this.anneeUniversitaireRepository = anneeUniversitaireRepository;
        this.departementRepository = departementRepository;
    }

    @Override
    public void insertModule(ModuleDto moduleDto) {
        moduleRepository.save(mapToModule(moduleDto));
    }

    @Override
    public void insertModules(List<ModuleDto> moduleDtos) {
        List<Module> modules = moduleDtos.stream()
            .map(this::mapToModule)
            .toList();
        moduleRepository.saveAll(modules);
    }

    @Override
    public ModuleDto updateModule(Long moduleId, ModuleDto moduleDto) {
        log.info("Updating module with ID: {}", moduleId);
        
        Module existingModule = moduleRepository.findById(moduleId)
            .orElseThrow(() -> new ModuleNotFoundException("Module not found with id=" + moduleId));

        // Update module properties
        existingModule.setNomModule(moduleDto.getNomModule());
        existingModule.setCodeModule(moduleDto.getCodeModule());
        existingModule.setVolumeHoraire(moduleDto.getVolumeHoraire());
        existingModule.setSemester(moduleDto.getSemester());
        existingModule.setHasTp(moduleDto.isHasTp());

        // Update relationships
        existingModule.setFiliere(filiereRepository.findById(moduleDto.getFiliere())
            .orElseThrow(() -> new FiliereNotFoundException("Filiere not found with id=" + moduleDto.getFiliere())));
        
        existingModule.setEnseignant(enseignantRepository.findById(moduleDto.getEnseignantid())
            .orElseThrow(() -> new EnseignantNotFoundException("Teacher not found with id=" + moduleDto.getEnseignantid())));

        // Save and return updated module as DTO
        return mapToDto(moduleRepository.save(existingModule));
    }

    @Override
    public ModuleDto getModulesById(Long moduleId) {
       return  mapToDto(moduleRepository.findById(moduleId).orElseThrow(()->new ModuleNotFoundException("module not found")));
    }

    @Override
    public void deleteModule(Long moduleId) {
        log.info("Deleting module with ID: {}", moduleId);
        
        if (!moduleRepository.existsById(moduleId)) {
            throw new ModuleNotFoundException("Module not found with id=" + moduleId);
        }
        
        moduleRepository.deleteById(moduleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleDto> getModulesByEnseignant(Integer enseignantId) {
        log.info("Fetching modules for teacher with ID: {}", enseignantId);
        
        // Verify teacher exists
        enseignantRepository.findById(enseignantId)
            .orElseThrow(() -> new EnseignantNotFoundException("Teacher not found with id=" + enseignantId));
            
        // Get current academic year
        AnneeUniversitaire currentYear = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));
            
        return moduleRepository.findByAnneeUniversitaire_AnneeIdAndEnseignant_EnseignantId(
            currentYear.getAnneeId(), enseignantId)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleDto> getModulesByDepartement(Integer departementId) {
        log.info("Fetching modules for department with ID: {}", departementId);
        
        // Verify department exists
        departementRepository.findById(departementId)
            .orElseThrow(() -> new DepartementNotFoundException("Department not found with id=" + departementId));
            
        // Get current academic year
        AnneeUniversitaire currentYear = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));
            
        // Get all teachers in the department
        List<Integer> enseignantIds = enseignantRepository.findByDepartement_DepartementId(departementId)
            .stream()
            .map(enseignant -> enseignant.getEnseignantId())
            .toList();
            
        // Get modules for all teachers in the department
        return moduleRepository.findByAnneeUniversitaire_AnneeIdAndEnseignant_EnseignantIdIn(
            currentYear.getAnneeId(), enseignantIds)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleDto> getModulesByFiliere(Integer filiereId) {
        log.info("Fetching modules for filiere with ID: {}", filiereId);
        
        // Verify filiere exists
        filiereRepository.findById(filiereId)
            .orElseThrow(() -> new FiliereNotFoundException("Filiere not found with id=" + filiereId));
            
        // Get current academic year
        AnneeUniversitaire currentYear = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));
            
        return moduleRepository.findByAnneeUniversitaire_AnneeIdAndFiliere_FiliereId(
            currentYear.getAnneeId(), filiereId)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<ModuleDto> getAllModules(){
        log.info("Fetching all modules ");

        return moduleRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Module mapToModule(ModuleDto moduleDto) {
        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        return new Module(
            moduleDto.getIdModule(),
            moduleDto.getNomModule(),
            moduleDto.getCodeModule(),
            moduleDto.getVolumeHoraire(),
            moduleDto.getSemester(),
            filiereRepository.findById(moduleDto.getFiliere())
                .orElseThrow(() -> new FiliereNotFoundException("Filiere not found with id=" + moduleDto.getFiliere())),
            enseignantRepository.findById(moduleDto.getEnseignantid())
                .orElseThrow(() -> new EnseignantNotFoundException("Teacher not found with id=" + moduleDto.getEnseignantid())),
            anneeUniversitaire,
            moduleDto.isHasTp()
        );
    }

    private ModuleDto mapToDto(Module module) {
        return ModuleDto.builder()
            .idModule(module.getIdModule())
            .nomModule(module.getNomModule())
            .codeModule(module.getCodeModule())
            .volumeHoraire(module.getVolumeHoraire())
            .semester(module.getSemester())
            .filiere(module.getFiliere().getFiliereId())
            .enseignantid(module.getEnseignant().getEnseignantId())
            .hasTp(module.isHasTp())
            .build();
    }
}
