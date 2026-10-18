package com.fst.rsi.dtos;

import com.fst.rsi.model.StatutClient;
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
public class ClientStatisticsDTO {
    private Long totalCommandes;
    private Long commandesTerminees;
    private Long commandesAnnulees;
    private BigDecimal depenseTotal;
    private BigDecimal panierMoyen;
    private Integer pointsFidelite;
    private StatutClient statutFidelite;
    private LocalDate dateInscription;
    private LocalDateTime dateDerniereCommande;
    private String platPrefere;
}