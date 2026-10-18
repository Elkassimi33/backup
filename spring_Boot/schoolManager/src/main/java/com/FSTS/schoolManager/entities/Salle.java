package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salles")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le nom de la salle ne peut pas être vide")
    @Column(unique = true, nullable = false)
    private String nom;

    @NotBlank(message = "Le code de la salle ne peut pas être vide")
    @Column(name = "code_salle", unique = true, nullable = false)
    private String codeSalle;

    @Min(value = 1, message = "La capacité doit être d'au moins 1 place")
    @Column(nullable = false)
    private Integer capacite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeSalle type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id")
    private Departement departement;

    @Column(name = "est_disponible", nullable = false)
    private Boolean estDisponible = true;

    @Column(name = "etage")
    private Integer etage;

    @Column(name = "bloc")
    private String bloc;

    @Column(name = "description")
    private String description;

    @Column(name = "equipements")
    private String equipements;

    public enum TypeSalle {
        COURS,
        TP,
        EXAMEN
    }
}