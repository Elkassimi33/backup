package com.FSTS.schoolManager.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "annees_universitaires", uniqueConstraints = {
        @UniqueConstraint(columnNames = "annee", name = "uk_annee_universitaire_annee")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AnneeUniversitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annee_id")
    private Integer anneeId;

    @NotBlank(message = "L'année universitaire est obligatoire")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "Le format de l'année doit être AAAA-AAAA (ex: 2023-2024)")
    @Column(nullable = false, length = 9)
    private String annee;

    @Column(name = "annee_courante", nullable = false)
    private boolean anneeCourante;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Valide si l'année de fin est bien l'année de début + 1
     * @return true si l'année est valide
     */
    @Transient
    public boolean isAnneeValide() {
        if (annee == null || !annee.matches("^\\d{4}-\\d{4}$")) {
            return false;
        }

        String[] parts = annee.split("-");
        int anneeDebut = Integer.parseInt(parts[0]);
        int anneeFin = Integer.parseInt(parts[1]);

        return anneeFin == anneeDebut + 1;
    }

    @Transient
    public Integer getAnneeDebut() {
        if (annee == null || !annee.matches("^\\d{4}-\\d{4}$")) {
            return null;
        }
        return Integer.parseInt(annee.split("-")[0]);
    }

    @Transient
    public Integer getAnneeFin() {
        if (annee == null || !annee.matches("^\\d{4}-\\d{4}$")) {
            return null;
        }
        return Integer.parseInt(annee.split("-")[1]);
    }
}