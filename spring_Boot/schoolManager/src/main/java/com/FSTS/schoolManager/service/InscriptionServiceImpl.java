package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.entities.*;
import com.FSTS.schoolManager.exception.*;
import com.FSTS.schoolManager.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EtudiantRepository etudiantRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;
    private final FiliereRepository filiereRepository;
    private final FiliereSemesterRepository filiereSemesterRepository;

    @Override
    public Inscription createInscription(Integer etudiantId, Integer anneeId, Integer filiereId) {
        if (isStudentEnrolled(etudiantId, anneeId)) {
            throw new InscriptionAlreadyExistsException("Student is already enrolled for this academic year");
        }

        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new EtudiantNotFoundException("Student not found with id=" + etudiantId));

        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findById(anneeId)
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Academic year not found with id=" + anneeId));

        Filiere filiere = filiereRepository.findById(filiereId)
            .orElseThrow(() -> new FiliereNotFoundException("Filiere not found with id=" + filiereId));

        Inscription inscription = new Inscription();
        inscription.setEtudiant(etudiant);
        inscription.setAnneeUniversitaire(anneeUniversitaire);
        inscription.setFiliere(filiere);
        inscription.setStatut(Inscription.StatutInscription.EN_COURS);

        // Update student status and filiere if they were VALIDE_NIVEAU
        if (etudiant.getStatut() == Etudiant.StatutEtudiant.VALIDE_NIVEAU) {
            updateStudentForNewFiliere(etudiant, filiereId);
        }

        return inscriptionRepository.save(inscription);
    }

    /**
     * Updates student's filiere, semester, and status when they change to a new filiere
     * @param etudiant the student to update
     * @param filiereId the new filiere id
     */
    private void updateStudentForNewFiliere(Etudiant etudiant, Integer filiereId) {
        // Get the first semester for this filiere
        FiliereSemester firstSemester = filiereSemesterRepository.findByFiliere_FiliereIdAndIsFirstSemesterTrue(filiereId)
            .orElseThrow(() -> new RuntimeException("No first semester found for filiere " + filiereId));

        Filiere newFiliere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new FiliereNotFoundException("Filiere not found with id=" + filiereId));

        etudiant.setFiliereID(newFiliere);
        etudiant.setSemester(firstSemester.getSemester());
        etudiant.setStatut(Etudiant.StatutEtudiant.NORMAL);
        etudiantRepository.save(etudiant);
        log.info("Updated student {} to new filiere {} with status NORMAL and semester {}", 
            etudiant.getCodeApj(), newFiliere.getFiliereId(), firstSemester.getSemester());
    }

    @Override
    @Transactional(readOnly = true)
    public Inscription getInscription(Integer etudiantId, Integer anneeId) {
        return inscriptionRepository.findByEtudiant_CodeApjAndAnneeUniversitaire_AnneeId(etudiantId, anneeId)
            .orElseThrow(() -> new InscriptionNotFoundException(
                "Inscription not found for student " + etudiantId + " and year " + anneeId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inscription> getStudentInscriptions(Integer etudiantId) {
        return inscriptionRepository.findByEtudiant_CodeApj(etudiantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inscription> getYearInscriptions(Integer anneeId) {
        return inscriptionRepository.findByAnneeUniversitaire_AnneeId(anneeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(Integer etudiantId, Integer anneeId) {
        return inscriptionRepository.existsByEtudiant_CodeApjAndAnneeUniversitaire_AnneeId(etudiantId, anneeId);
    }
} 