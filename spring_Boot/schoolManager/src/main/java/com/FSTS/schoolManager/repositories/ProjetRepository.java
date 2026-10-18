package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Projet;
import com.FSTS.schoolManager.entities.Projet.StatutProjet;
import com.FSTS.schoolManager.entities.Projet.TypeProjet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Integer> {

    /**
     * Trouve des projets par titre
     * @param titre le titre à rechercher
     * @return la liste des projets correspondants
     */
    List<Projet> findByTitreContainingIgnoreCase(String titre);

    /**
     * Trouve des projets par type
     * @param type le type de projet
     * @return la liste des projets de ce type
     */
    List<Projet> findByType(TypeProjet type);

    /**
     * Trouve des projets par statut
     * @param statut le statut du projet
     * @return la liste des projets avec ce statut
     */
    List<Projet> findByStatut(StatutProjet statut);

    /**
     * Trouve des projets par encadrant interne
     * @param enseignantId l'ID de l'enseignant encadrant
     * @return la liste des projets encadrés par cet enseignant
     */
    List<Projet> findByEncadrantInterne_EnseignantId(Integer enseignantId);

    /**
     * Trouve des projets par filière
     * @param filiereId l'ID de la filière
     * @return la liste des projets pour cette filière
     */
    List<Projet> findByFiliere_FiliereId(Integer filiereId);

    /**
     * Trouve des projets par année universitaire
     * @param anneeUniversitaireId l'ID de l'année universitaire
     * @return la liste des projets pour cette année universitaire
     */
    List<Projet> findByAnneeUniversitaire_AnneeId(Integer anneeUniversitaireId);

    /**
     * Trouve des projets par étudiant
     * @param etudiantId l'ID de l'étudiant
     * @return la liste des projets auxquels cet étudiant participe
     */
    @Query("SELECT p FROM Projet p JOIN p.etudiants e WHERE e.codeApj = ?1")
    List<Projet> findByEtudiantId(Integer etudiantId);

    /**
     * Trouve des projets par entreprise
     * @param entreprise le nom de l'entreprise
     * @return la liste des projets dans cette entreprise
     */
    List<Projet> findByEntrepriseContainingIgnoreCase(String entreprise);

    /**
     * Trouve des projets dont la soutenance est prévue entre deux dates
     * @param dateDebut la date de début
     * @param dateFin la date de fin
     * @return la liste des projets avec soutenance dans cette période
     */
    List<Projet> findByDateSoutenanceBetween(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Trouve des projets par type et statut
     * @param type le type de projet
     * @param statut le statut du projet
     * @return la liste des projets correspondants
     */
    List<Projet> findByTypeAndStatut(TypeProjet type, StatutProjet statut);

    /**
     * Trouve des projets par membre du jury
     * @param enseignantId l'ID de l'enseignant membre du jury
     * @return la liste des projets où cet enseignant est membre du jury
     */
    @Query("SELECT p FROM Projet p JOIN p.jury j WHERE j.enseignantId = ?1")
    List<Projet> findByJuryMemberId(Integer enseignantId);
}