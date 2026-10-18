package com.fst.rsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportVente {

    private Long idRapport;

    private Long managerId;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private LocalDateTime dateGeneration = LocalDateTime.now();

    private TypeRapport typeRapport;

    private BigDecimal chiffreAffaireTotal;

    private Integer nombreCommandesTotal;

    private Integer nombreClientsUniques;

    private BigDecimal panierMoyen;

    private BigDecimal chiffreAffaireSurPlace;

    private BigDecimal chiffreAffaireAEmporter;

    private BigDecimal chiffreAffaireLivraison;

    private String platsPlusVendus;

    private String observationsManager;

    private String recommendations;
}

