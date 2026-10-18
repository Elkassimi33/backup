package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Integer> {

    /**
     * Trouve un département par son nom
     * @param nom le nom du département à rechercher
     * @return un Optional contenant le département s'il existe
     */
    Optional<Departement> findByNom(String nom);

    /**
     * Trouve des départements dont le nom contient la chaîne spécifiée
     * @param searchTerm la chaîne à rechercher
     * @return la liste des départements correspondants
     */
    List<Departement> findByNomContainingIgnoreCase(String searchTerm);

    /**
     * Trouve des départements par l'ID de leur responsable
     * @param responsableId l'ID du responsable
     * @return un Optional contenant le département s'il existe
     */
    Optional<Departement> findByResponsable_Id(Integer responsableId);

    /**
     * Vérifie si un département avec le nom spécifié existe
     * @param nom le nom à vérifier
     * @return true si un département avec ce nom existe
     */
    boolean existsByNom(String nom);
}