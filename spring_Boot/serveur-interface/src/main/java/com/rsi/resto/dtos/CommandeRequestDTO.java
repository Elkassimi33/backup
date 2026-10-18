package com.rsi.resto.dtos;

import com.rsi.resto.enums.TypeCommande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeRequestDTO {

    private Long idClient;

    private Long idServeur;

    private Long idTable;

    private TypeCommande typeCommande;

    private List<LigneCommandeRequestDTO> lignesCommande;

    private String instructionsSpeciales;

    private BigDecimal remise;
}