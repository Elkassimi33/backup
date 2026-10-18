package com.FSTS.schoolManager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiliereSemester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;

    @Enumerated(EnumType.STRING)
    private Module.Semester semester;

    @Column(name = "is_first_semester")
    private boolean isFirstSemester;

    @Column(name = "is_last_semester")
    private boolean isLastSemester;
} 