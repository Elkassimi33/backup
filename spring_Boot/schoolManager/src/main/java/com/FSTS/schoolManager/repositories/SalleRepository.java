package com.FSTS.schoolManager.repositories;

import com.FSTS.schoolManager.entities.Salle;
import com.FSTS.schoolManager.entities.Salle.TypeSalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Integer> {

    /**
     * Trouve une salle par son nom
     * @param nom le nom de la salle
     * @return un Optional contenant la salle si elle existe
     */
    Optional<Salle> findByNom(String nom);

    /**
     * Trouve une salle par son code
     * @param codeSalle le code de la salle
     * @return un Optional contenant la salle si elle existe
     */
    Optional<Salle> findByCodeSalle(String codeSalle);

    /**
     * Trouve des salles par type
     * @param type le type de salle
     * @return la liste des salles de ce type
     */
    List<Salle> findByType(TypeSalle type);

    /**
     * Trouve des salles par disponibilité
     * @param estDisponible true pour les salles disponibles, false pour les indisponibles
     * @return la liste des salles selon leur disponibilité
     */
    List<Salle> findByEstDisponible(Boolean estDisponible);

    /**
     * Trouve des salles par département
     * @param departementId l'ID du département
     * @return la liste des salles de ce département
     */
    List<Salle> findByDepartement_DepartementId(Integer departementId);

    /**
     * Trouve des salles par étage
     * @param etage l'étage des salles
     * @return la liste des salles à cet étage
     */
    List<Salle> findByEtage(Integer etage);

    /**
     * Trouve des salles par bloc
     * @param bloc le bloc des salles
     * @return la liste des salles dans ce bloc
     */
    List<Salle> findByBloc(String bloc);

    /**
     * Trouve des salles dont la capacité est supérieure ou égale à la valeur spécifiée
     * @param capaciteMin la capacité minimale
     * @return la liste des salles correspondantes
     */
    List<Salle> findByCapaciteGreaterThanEqual(Integer capaciteMin);

    /**
     * Trouve des salles par type et disponibilité
     * @param type le type de salle
     * @param estDisponible true pour les salles disponibles, false pour les indisponibles
     * @return la liste des salles correspondantes
     */
    List<Salle> findByTypeAndEstDisponible(TypeSalle type, Boolean estDisponible);

    List<Salle> findByCapaciteGreaterThanEqualAndEstDisponibleTrue(Integer capacite);
}