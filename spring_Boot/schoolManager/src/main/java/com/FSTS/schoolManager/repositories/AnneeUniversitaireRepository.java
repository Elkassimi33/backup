package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.AnneeUniversitaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnneeUniversitaireRepository extends JpaRepository<AnneeUniversitaire, Integer> {

    /**
     * Trouve une année universitaire par sa notation (ex: "2023-2024")
     * @param annee la notation de l'année
     * @return un Optional contenant l'année universitaire si elle existe
     */
    Optional<AnneeUniversitaire> findByAnnee(String annee);

    /**
     * Trouve l'année universitaire courante
     * @return un Optional contenant l'année universitaire courante si elle existe
     */
    Optional<AnneeUniversitaire> findByAnneeCouranteIsTrue();

    /**
     * Vérifie si une année universitaire avec la notation spécifiée existe
     * @param annee la notation de l'année à vérifier
     * @return true si une année universitaire avec cette notation existe
     */
    boolean existsByAnnee(String annee);

    /**
     * Compte le nombre d'années universitaires marquées comme courantes
     * @return le nombre d'années universitaires courantes
     */
    long countByAnneeCouranteIsTrue();
}