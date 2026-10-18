package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.auth.repositories.UserRepository;
import com.FSTS.schoolManager.dtos.DepartementDto;
import com.FSTS.schoolManager.entities.Departement;
import com.FSTS.schoolManager.exception.DepartementNotFoundException;
import com.FSTS.schoolManager.repositories.DepartementRepository;
import com.FSTS.schoolManager.repositories.EnseignantRepository;
import com.FSTS.schoolManager.repositories.FiliereRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DepartementServiceImpl implements DepartementService {

    private final DepartementRepository departementRepository;
    private final EnseignantRepository enseignantRepository;
    private final FiliereRepository filiereRepository;
    private final UserRepository userRepository;

    @Override
    public DepartementDto createDepartement(DepartementDto departementDto) {
        log.info("Creating new department: {}", departementDto.getNom());
        Departement departement = mapToEntity(departementDto);
//        departement.setNom("mat");
        return mapToDto(departementRepository.save(departement));
    }

    @Override
    public DepartementDto updateDepartement(Integer departementId, DepartementDto departementDto) {
        log.info("Updating department with ID: {}", departementId);
        
        Departement existingDepartement = departementRepository.findById(departementId)
            .orElseThrow(() -> new DepartementNotFoundException("Department not found with id=" + departementId));

        existingDepartement.setNom(departementDto.getNom());
        existingDepartement.setDescription(departementDto.getDescription());

        return mapToDto(departementRepository.save(existingDepartement));
    }

    @Override
    public void deleteDepartement(Integer departementId) {
        log.info("Deleting department with ID: {}", departementId);
        
        if (!departementRepository.existsById(departementId)) {
            throw new DepartementNotFoundException("Department not found with id=" + departementId);
        }
        
        departementRepository.deleteById(departementId);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartementDto getDepartementById(Integer departementId) {
        log.info("Fetching department with ID: {}", departementId);
        return mapToDto(departementRepository.findById(departementId)
            .orElseThrow(() -> new DepartementNotFoundException("Department not found with id=" + departementId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartementDto> getAllDepartements() {
        log.info("Fetching all departments");
        return departementRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartementDto getDepartementByNom(String nom) {
        log.info("Fetching department with name: {}", nom);
        return mapToDto(departementRepository.findByNom(nom)
            .orElseThrow(() -> new DepartementNotFoundException("Department not found with name=" + nom)));
    }

    private Departement mapToEntity(DepartementDto dto) {
        return Departement.builder()
            .departementId(null)
            .nom(dto.getNom())
            .description(dto.getDescription())
            .responsable(userRepository.findByUsername(dto.getChefDepartementUsername())
                        .orElseThrow(()->new UsernameNotFoundException("user not found with id="+dto.getChefDepartementUsername())))
            .build();
    }

    private DepartementDto mapToDto(Departement departement) {
        return DepartementDto.builder()
            .departementId(departement.getDepartementId())
            .nom(departement.getNom())
            .description(departement.getDescription())
            .ChefDepartementUsername(departement.getResponsable().getUsername())
            .nombreEnseignants((int) enseignantRepository.countByDepartement_DepartementId(departement.getDepartementId()))
            .nombreFilieres((int) filiereRepository.countByDepartement_DepartementId(departement.getDepartementId()))
            .build();
    }
} 