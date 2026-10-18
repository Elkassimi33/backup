package com.FSTS.schoolManager.entities;

import com.FSTS.schoolManager.auth.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;

@Entity
@Table(name = "enseignants", uniqueConstraints = {
        @UniqueConstraint(columnNames = "telephone", name = "uk_enseignant_telephone"),
        @UniqueConstraint(columnNames = "user_id", name = "uk_enseignant_user_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {"user", "departement"})
public class Enseignant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enseignant_id")
    private Integer enseignantId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "L'utilisateur est obligatoire")
    private User user;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    @Column(nullable = false, length = 50)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    @Column(nullable = false, length = 50)
    private String prenom;

    @Size(max = 100, message = "Le profil ne doit pas dépasser 100 caractères")
    @Column(length = 100)
    private String profil;

    @Size(max = 100, message = "La spécialité ne doit pas dépasser 100 caractères")
    @Column(length = 100)
    private String specialite;

    @Size(max = 50, message = "Le grade ne doit pas dépasser 50 caractères")
    @Column(length = 50)
    private String grade;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Le numéro de téléphone doit être au format valide")
    @Column(unique = true, length = 20)
    private String telephone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id")
    private Departement departement;

//    @OneToMany(mappedBy = "enseignant")
//    @Builder.Default
//    private List<Module> modulesTaught = new ArrayList<>();
//
//    @OneToMany(mappedBy = "chefFiliere")
//    @Builder.Default
//    private List<Filiere> filieresDirigees = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 20)
    private StatutEnseignant statut;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt=LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Les différents statuts possibles pour un enseignant
     */
    public enum StatutEnseignant {
        PERMANENT,
        VACATAIRE,
        ASSOCIE,
        INVITE,
        EMERITE
    }

    /**
     * Retourne le nom complet de l'enseignant
     * @return le nom complet (prénom + nom)
     */
    @Transient
    public String getNomComplet() {
        return prenom + " " + nom;
    }

//    /**
//     * Ajoute un module aux enseignements de ce professeur
//     * @param module Le module à ajouter
//     * @return true si l'ajout a réussi, false si le module était déjà présent
//     */
//    public boolean ajouterModule(Module module) {
//        if (modulesTaught.contains(module)) {
//            return false;
//        }
//
//        module.setEnseignant(this);
//        return modulesTaught.add(module);
//    }
//
//    /**
//     * Supprime un module des enseignements de ce professeur
//     * @param module Le module à supprimer
//     * @return true si la suppression a réussi, false si le module n'était pas présent
//     */
//    public boolean supprimerModule(Module module) {
//        if (modulesTaught.contains(module)) {
//            module.setEnseignant(null);
//            return modulesTaught.remove(module);
//        }
//        return false;
//    }
//
//    /**
//     * Vérifie si l'enseignant est chef d'une filière
//     * @return true si l'enseignant est chef d'au moins une filière
//     */
//    @Transient
//    public boolean isChefFiliere() {
//        return !filieresDirigees.isEmpty();
//    }
//
//    /**
//     * Retourne le nombre de modules enseignés
//     * @return le nombre de modules
//     */
//    @Transient
//    public int getNombreModules() {
//        return modulesTaught.size();
//    }

    /**
     * Retourne l'email de l'enseignant via son compte utilisateur
     * @return l'email ou null si le compte utilisateur n'est pas disponible
     */
    @Transient
    public String getEmail() {
        return user != null ? user.getEmail() : null;
    }
}
