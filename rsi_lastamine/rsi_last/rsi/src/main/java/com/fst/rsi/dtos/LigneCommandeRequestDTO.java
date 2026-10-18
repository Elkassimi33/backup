package com.fst.rsi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeRequestDTO {
    private Long idPlat;
    private Integer quantite;
    private String options;
    private BigDecimal prixUnitaire;
}