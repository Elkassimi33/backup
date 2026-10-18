package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    /**
     * Trouve un administrateur par son numéro de téléphone
     * @param telephone le numéro de téléphone à rechercher
     * @return un Optional contenant l'administrateur s'il existe
     */
    Optional<Admin> findByTelephone(String telephone);

    /**
     * Trouve un administrateur par l'ID de son utilisateur associé
     * @param userId l'ID de l'utilisateur
     * @return un Optional contenant l'administrateur s'il existe
     */
    Optional<Admin> findByUser_Id(Integer userId);

    /**
     * Trouve un administrateur par l'email de son utilisateur associé
     * @param email l'email à rechercher
     * @return un Optional contenant l'administrateur s'il existe
     */
    Optional<Admin> findByUser_Email(String email);

    /**
     * Trouve tous les administrateurs dont le nom ou le prénom contient la chaîne spécifiée
     * @param searchTerm la chaîne à rechercher
     * @return une liste d'administrateurs correspondant au critère de recherche
     */
    java.util.List<Admin> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String searchTerm, String searchTerm2);
}