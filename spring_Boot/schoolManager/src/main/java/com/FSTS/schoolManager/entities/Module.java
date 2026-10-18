package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "modules")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idModule;

    @Column(nullable = false, length = 100)
    private String nomModule;

    @Column(unique = true, length = 20)
    private String codeModule;

    @Column(name = "volume_horaire")
    private Integer volumeHoraire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annee_universitaire_id")
    private AnneeUniversitaire anneeUniversitaire;

    @Column(name = "has_tp", nullable = false)
    private boolean hasTp = false;



    public enum Semester {
        S1, S2, S3, S4, S5, S6, S7, S8, S9, S10
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return idModule != null && idModule.equals(module.idModule);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Module{" +
                "idModule=" + idModule +
                ", nomModule='" + nomModule + '\'' +
                ", codeModule='" + codeModule + '\'' +
                ", semester=" + semester +
                '}';
    }
}