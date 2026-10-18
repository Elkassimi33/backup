package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Filiere;
import com.FSTS.schoolManager.entities.Filiere.NiveauFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Integer> {

    /**
     * Trouve une filière par son nom
     * @param nom le nom de la filière
     * @return un Optional contenant la filière si elle existe
     */
    Optional<Filiere> findByNom(String nom);

    /**
     * Trouve des filières dont le nom contient la chaîne spécifiée
     * @param searchTerm la chaîne à rechercher
     * @return la liste des filières correspondantes
     */
    List<Filiere> findByNomContainingIgnoreCase(String searchTerm);

    /**
     * Trouve des filières par niveau
     * @param niveau le niveau de la filière
     * @return la liste des filières de ce niveau
     */
    List<Filiere> findByNiveau(NiveauFiliere niveau);

    /**
     * Trouve des filières par département
     * @param departementId l'ID du département
     * @return la liste des filières de ce département
     */
    List<Filiere> findByDepartement_DepartementId(Integer departementId);

    /**
     * Trouve des filières par chef de filière
     * @param enseignantId l'ID de l'enseignant chef de filière
     * @return la liste des filières dirigées par cet enseignant
     */
    List<Filiere> findByChefFiliere_EnseignantId(Integer enseignantId);

    /**
     * Trouve des filières par état d'activation
     * @param active true pour les filières actives, false pour les inactives
     * @return la liste des filières selon leur état d'activation
     */
    List<Filiere> findByActive(Boolean active);

    /**
     * Vérifie si une filière avec le nom spécifié existe déjà dans un département
     * @param nom le nom de la filière
     * @param niveau le niveau de la filière
     * @param departementId l'ID du département
     * @return true si une filière avec ce nom et ce niveau existe déjà dans ce département
     */
    boolean existsByNomAndNiveauAndDepartement_DepartementId(String nom, NiveauFiliere niveau, Integer departementId);

    /**
     * Trouve des filières par durée d'études
     * @param dureeEtudes la durée d'études en années
     * @return la liste des filières avec cette durée d'études
     */
    List<Filiere> findByDureeEtudes(Integer dureeEtudes);

    long countByDepartement_DepartementId(Integer departementId);

    List<Filiere> findByActiveTrue();
}