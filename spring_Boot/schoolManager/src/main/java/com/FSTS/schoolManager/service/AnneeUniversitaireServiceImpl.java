package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.entities.AnneeUniversitaire;
import com.FSTS.schoolManager.exception.AnneeUniversitaireNotFoundException;
import com.FSTS.schoolManager.repositories.AnneeUniversitaireRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AnneeUniversitaireServiceImpl implements AnneeUniversitaireService {

    private final AnneeUniversitaireRepository anneeUniversitaireRepository;

    @Override
    public AnneeUniversitaire createAnneeUniversitaire(AnneeUniversitaire anneeUniversitaire) {
        log.info("Creating new academic year: {}", anneeUniversitaire.getAnneeId());
        
        // If this is set as current year, unset any existing current year
        if (anneeUniversitaire.isAnneeCourante()) {
            anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
                .ifPresent(currentYear -> {
                    currentYear.setAnneeCourante(false);
                    anneeUniversitaireRepository.save(currentYear);
                });
        }

        anneeUniversitaire.setCreatedAt(LocalDateTime.now());
        
        return anneeUniversitaireRepository.save(anneeUniversitaire);
    }

    @Override
    public AnneeUniversitaire updateAnneeUniversitaire(Integer anneeId, AnneeUniversitaire anneeUniversitaire) {
        log.info("Updating academic year with ID: {}", anneeId);
        
        AnneeUniversitaire existingAnnee = anneeUniversitaireRepository.findById(anneeId)
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Academic year not found with id=" + anneeId));

        // If setting as current year, unset any existing current year
        if (anneeUniversitaire.isAnneeCourante() && !existingAnnee.isAnneeCourante()) {
            anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
                .ifPresent(currentYear -> {
                    currentYear.setAnneeCourante(false);
                    anneeUniversitaireRepository.save(currentYear);
                });
        }

        existingAnnee.setAnneeId(anneeUniversitaire.getAnneeId());
        existingAnnee.setAnneeCourante(anneeUniversitaire.isAnneeCourante());
        existingAnnee.setDateDebut(anneeUniversitaire.getDateDebut());
        existingAnnee.setDateFin(anneeUniversitaire.getDateFin());
        existingAnnee.setUpdatedAt(LocalDateTime.now());

        return anneeUniversitaireRepository.save(existingAnnee);
    }

    @Override
    public void deleteAnneeUniversitaire(Integer anneeId) {
        log.info("Deleting academic year with ID: {}", anneeId);
        
        if (!anneeUniversitaireRepository.existsById(anneeId)) {
            throw new AnneeUniversitaireNotFoundException("Academic year not found with id=" + anneeId);
        }
        
        anneeUniversitaireRepository.deleteById(anneeId);
    }

    @Override
    @Transactional(readOnly = true)
    public AnneeUniversitaire getAnneeUniversitaireById(Integer anneeId) {
        log.info("Fetching academic year with ID: {}", anneeId);
        return anneeUniversitaireRepository.findById(anneeId)
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Academic year not found with id=" + anneeId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnneeUniversitaire> getAllAnneesUniversitaires() {
        log.info("Fetching all academic years");
        return anneeUniversitaireRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AnneeUniversitaire getCurrentAnneeUniversitaire() {
        log.info("Fetching current academic year");
        return anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("No current academic year found"));
    }

    @Override
    public void setCurrentAnneeUniversitaire(Integer anneeId) {
        log.info("Setting academic year {} as current", anneeId);
        
        AnneeUniversitaire newCurrentYear = anneeUniversitaireRepository.findById(anneeId)
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Academic year not found with id=" + anneeId));

        // Unset current year
        anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .ifPresent(currentYear -> {
                currentYear.setAnneeCourante(false);
                anneeUniversitaireRepository.save(currentYear);
            });

        // Set new current year
        newCurrentYear.setAnneeCourante(true);
        anneeUniversitaireRepository.save(newCurrentYear);
    }
} 