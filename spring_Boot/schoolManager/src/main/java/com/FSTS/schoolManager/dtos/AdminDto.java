package com.FSTS.schoolManager.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private Integer id;
    private String email;
    private String nom;
    private String prenom;
    private String specialite;
    private String grade;
    private String username;
    private String universityEmail;
} 