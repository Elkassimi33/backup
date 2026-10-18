package com.FSTS.schoolManager.entities;

import com.FSTS.schoolManager.auth.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departements", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nom", name = "uk_departement_nom")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {"responsable", "filieres"})
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "departement_id")
    private Integer departementId;

    @NotBlank(message = "Le nom du département est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom du département doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private User responsable;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Filiere> filieres = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Ajoute une filière au département
     * @param filiere La filière à ajouter
     * @return true si l'ajout a réussi, false si la filière était déjà présente
     */
    public boolean ajouterFiliere(Filiere filiere) {
        if (filieres.contains(filiere)) {
            return false;
        }

        filiere.setDepartement(this);
        return filieres.add(filiere);
    }

    /**
     * Supprime une filière du département
     * @param filiere La filière à supprimer
     * @return true si la suppression a réussi, false si la filière n'était pas présente
     */
    public boolean supprimerFiliere(Filiere filiere) {
        if (filieres.contains(filiere)) {
            filiere.setDepartement(null);
            return filieres.remove(filiere);
        }
        return false;
    }

    /**
     * Retourne le nombre de filières dans ce département
     * @return le nombre de filières
     */
    @Transient
    public int getNombreFilieres() {
        return filieres.size();
    }

    /**
     * Vérifie si le département a un responsable
     * @return true si un responsable est assigné, false sinon
     */
    @Transient
    public boolean hasResponsable() {
        return responsable != null;
    }
}