package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.SalleDto;
import com.FSTS.schoolManager.entities.Salle;
import com.FSTS.schoolManager.exception.SalleNotFoundException;
import com.FSTS.schoolManager.repositories.SalleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;

    @Override
    public Salle createSalle(SalleDto salleDto) {
        log.info("Creating new classroom: {}", salleDto.getNomSalle());
        Salle salle = mapToEntity(salleDto);
        return salleRepository.save(salle);
    }

    @Override
    public Salle updateSalle(Integer salleId, SalleDto salleDto) {
        log.info("Updating classroom with ID: {}", salleId);
        
        Salle existingSalle = salleRepository.findById(salleId)
            .orElseThrow(() -> new SalleNotFoundException("Classroom not found with id=" + salleId));

        existingSalle.setNom(salleDto.getNomSalle());
        existingSalle.setCapacite(salleDto.getCapacite());
        existingSalle.setType(salleDto.getType());
        existingSalle.setEstDisponible(salleDto.isDisponible());

        return salleRepository.save(existingSalle);
    }

    @Override
    public void deleteSalle(Integer salleId) {
        log.info("Deleting classroom with ID: {}", salleId);
        
        if (!salleRepository.existsById(salleId)) {
            throw new SalleNotFoundException("Classroom not found with id=" + salleId);
        }
        
        salleRepository.deleteById(salleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Salle getSalleById(Integer salleId) {
        log.info("Fetching classroom with ID: {}", salleId);
        return salleRepository.findById(salleId)
            .orElseThrow(() -> new SalleNotFoundException("Classroom not found with id=" + salleId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Salle> getAllSalles() {
        log.info("Fetching all classrooms");
        return salleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Salle> getAvailableSallesByCapacity(Integer capacity) {
        log.info("Fetching available classrooms with capacity >= {}", capacity);
        return salleRepository.findByCapaciteGreaterThanEqualAndEstDisponibleTrue(capacity);
    }

    private Salle mapToEntity(SalleDto dto) {
        return Salle.builder()
            .nom(dto.getNomSalle())
            .capacite(dto.getCapacite())
            .type(dto.getType())
            .estDisponible(dto.isDisponible())
            .build();
    }
} 