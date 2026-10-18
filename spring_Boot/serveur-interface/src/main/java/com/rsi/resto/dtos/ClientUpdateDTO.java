package com.rsi.resto.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDTO {

    private String nom;
    private String prenom;
    private String telephone;

    private String adresse;
    private String photo;
    private String preferences;
}
