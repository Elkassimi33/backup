package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Enseignant;
import com.FSTS.schoolManager.entities.Enseignant.StatutEnseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Integer> {

    /**
     * Trouve un enseignant par son numéro de téléphone
     * @param telephone le numéro de téléphone à rechercher
     * @return un Optional contenant l'enseignant s'il existe
     */
    Optional<Enseignant> findByTelephone(String telephone);

    /**
     * Trouve un enseignant par l'ID de son utilisateur associé
     * @param userId l'ID de l'utilisateur
     * @return un Optional contenant l'enseignant s'il existe
     */
    Optional<Enseignant> findByUser_Id(Integer userId);

    /**
     * Trouve un enseignant par l'email de son utilisateur associé
     * @param email l'email à rechercher
     * @return un Optional contenant l'enseignant s'il existe
     */
    Optional<Enseignant> findByUser_Email(String email);

    /**
     * Trouve tous les enseignants d'un département spécifique
     * @param departementId l'ID du département
     * @return la liste des enseignants du département
     */
    List<Enseignant> findByDepartement_DepartementId(Integer departementId);

    /**
     * Trouve tous les enseignants par statut
     * @param statut le statut des enseignants à rechercher
     * @return la liste des enseignants ayant ce statut
     */
    List<Enseignant> findByStatut(StatutEnseignant statut);

    /**
     * Trouve tous les enseignants dont le nom ou le prénom contient la chaîne spécifiée
     * @param searchTerm la chaîne à rechercher
     * @return une liste d'enseignants correspondant au critère de recherche
     */
    List<Enseignant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String searchTerm, String searchTerm2);

    /**
     * Trouve tous les enseignants par leur spécialité
     * @param specialite la spécialité à rechercher
     * @return la liste des enseignants ayant cette spécialité
     */
    List<Enseignant> findBySpecialiteContainingIgnoreCase(String specialite);

    long countByDepartement_DepartementId(Integer departementId);
}