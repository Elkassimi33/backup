package com.fst.rsi.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDTO {

    private String nom;

    private String prenom;

    private String email;

    private String telephone;

    private String password;

    private String adresse;
    private String preferences;

}