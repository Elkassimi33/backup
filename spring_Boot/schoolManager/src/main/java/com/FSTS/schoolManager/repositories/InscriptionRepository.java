package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Inscription;
import com.FSTS.schoolManager.entities.Inscription.StatutInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {

    /**
     * Trouve une inscription par étudiant et année universitaire
     * @param codeApj le code de l'étudiant
     * @param anneeId l'ID de l'année universitaire
     * @return un Optional contenant l'inscription si elle existe
     */
    Optional<Inscription> findByEtudiant_CodeApjAndAnneeUniversitaire_AnneeId(Integer codeApj, Integer anneeId);

    /**
     * Trouve toutes les inscriptions d'un étudiant
     * @param codeApj le code de l'étudiant
     * @return la liste des inscriptions de cet étudiant
     */
    List<Inscription> findByEtudiant_CodeApj(Integer codeApj);

    /**
     * Trouve toutes les inscriptions pour une filière et une année universitaire
     * @param filiereId l'ID de la filière
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return la liste des inscriptions pour cette filière et cette année universitaire
     */
    List<Inscription> findByFiliere_FiliereIdAndAnneeUniversitaire_AnneeId(Integer filiereId, Integer anneeUniversitaireId);

    /**
     * Trouve toutes les inscriptions pour une année universitaire
     * @param anneeId l'ID de l'année universitaire
     * @return la liste des inscriptions pour cette année universitaire
     */
    List<Inscription> findByAnneeUniversitaire_AnneeId(Integer anneeId);

    /**
     * Trouve toutes les inscriptions par statut
     * @param statut le statut des inscriptions
     * @return la liste des inscriptions ayant ce statut
     */
    List<Inscription> findByStatut(StatutInscription statut);

    /**
     * Compte le nombre d'inscriptions pour une filière et une année universitaire
     * @param filiereId l'ID de la filière
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return le nombre d'inscriptions
     */
    long countByFiliere_FiliereIdAndAnneeUniversitaire_AnneeId(Integer filiereId, Integer anneeUniversitaireId);

    /**
     * Vérifie si un étudiant est déjà inscrit pour une année universitaire
     * @param codeApj le code de l'étudiant
     * @param anneeId l'ID de l'année universitaire
     * @return true si l'étudiant est déjà inscrit
     */
    boolean existsByEtudiant_CodeApjAndAnneeUniversitaire_AnneeId(Integer codeApj, Integer anneeId);
}