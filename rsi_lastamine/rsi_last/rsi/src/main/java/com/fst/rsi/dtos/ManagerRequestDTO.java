package com.fst.rsi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRequestDTO {

    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String password;

    private String adresse;
    private String photo;
    private LocalDate dateEmbauche;

    private BigDecimal salaire;

    private String specialites;
}

