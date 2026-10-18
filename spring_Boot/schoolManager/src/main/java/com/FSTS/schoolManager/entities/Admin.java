package com.FSTS.schoolManager.entities;

import com.FSTS.schoolManager.auth.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins", uniqueConstraints = {
        @UniqueConstraint(columnNames = "telephone", name = "uk_admin_telephone")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "user")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer adminId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt=LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Retourne le nom complet de l'administrateur
     * @return le nom complet (prénom + nom)
     */
    @Transient
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    /**
     * Retourne l'email de l'administrateur via son compte utilisateur
     * @return l'email ou null si le compte utilisateur n'est pas disponible
     */
    @Transient
    public String getEmail() {
        return user != null ? user.getEmail() : null;
    }
}