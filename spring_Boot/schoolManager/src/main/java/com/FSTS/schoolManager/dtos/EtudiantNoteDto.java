package com.FSTS.schoolManager.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantNoteDto {

    @NotBlank(message = "Le codeApj ne peut pas être vide")
    private Integer codeApj;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;

    private Double noteExam;

    private Double noteTp;

}
