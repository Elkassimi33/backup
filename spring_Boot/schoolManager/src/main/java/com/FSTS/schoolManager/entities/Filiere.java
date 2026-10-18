package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "filieres", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nom", "niveau", "departement_id"}, name = "uk_filiere_nom_niveau_departement")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {"departement", "chefFiliere", "modules"})
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "filiere_id")
    private Integer filiereId;

    @NotBlank(message = "Le nom de la filière est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom de la filière doit contenir entre 2 et 100 caractères")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    @NotNull(message = "La durée des études est obligatoire")
    @Positive(message = "La durée des études doit être positive")
    @Column(name = "duree_etudes", nullable = false)
    private Integer dureeEtudes;

    @NotNull(message = "Le niveau de la filière est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NiveauFiliere niveau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_filiere_id")
    private Enseignant chefFiliere;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Module> modules = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Les différents niveaux de filière disponibles
     */
    public enum NiveauFiliere {
        DEUST("Diplôme d'Études Universitaires Scientifiques et Techniques"),
        LST("Licence Sciences et Techniques"),
        CYCLE("Cycle d'Ingénieur"),
        MASTER("Master"),
        DOCTORAT("Doctorat");

        private final String libelle;

        NiveauFiliere(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    /**
     * Ajoute un module à la filière
     * @param module Le module à ajouter
     * @return true si l'ajout a réussi, false si le module était déjà présent
     */
    public boolean ajouterModule(Module module) {
        if (modules.contains(module)) {
            return false;
        }

        module.setFiliere(this);
        return modules.add(module);
    }

    /**
     * Supprime un module de la filière
     * @param module Le module à supprimer
     * @return true si la suppression a réussi, false si le module n'était pas présent
     */
    public boolean supprimerModule(Module module) {
        if (modules.contains(module)) {
            module.setFiliere(null);
            return modules.remove(module);
        }
        return false;
    }

    /**
     * Retourne le nombre total de modules dans cette filière
     * @return le nombre de modules
     */
    @Transient
    public int getNombreModules() {
        return modules.size();
    }

    /**
     * Retourne le nom complet de la filière avec son niveau
     * @return nom de la filière avec son niveau
     */
    @Transient
    public String getNomComplet() {
        return nom + " (" + niveau.name() + ")";
    }

    /**
     * Vérifie si la filière a un chef assigné
     * @return true si un chef de filière est assigné, false sinon
     */
    @Transient
    public boolean hasChefFiliere() {
        return chefFiliere != null;
    }

    /**
     * Active la filière
     */
    public void activer() {
        this.active = true;
    }

    /**
     * Désactive la filière
     */
    public void desactiver() {
        this.active = false;
    }
}