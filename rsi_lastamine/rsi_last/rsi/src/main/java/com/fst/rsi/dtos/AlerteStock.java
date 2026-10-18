package com.fst.rsi.dtos;

import com.fst.rsi.model.StatutAlerte;
import com.fst.rsi.model.TypeAlerte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlerteStock {

    private Long idAlerte;
    private Long ingredientId;

    private TypeAlerte typeAlerte;

    private LocalDateTime dateAlerte = LocalDateTime.now();

    private StatutAlerte statut = StatutAlerte.NON_TRAITEE;

    private String traiteePar;

    private LocalDateTime dateTraitement;

    private String actionPrise;

    private String message;
}