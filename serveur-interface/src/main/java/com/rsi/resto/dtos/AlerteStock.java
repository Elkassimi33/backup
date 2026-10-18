package com.rsi.resto.dtos;

import com.rsi.resto.enums.StatutAlerte;
import com.rsi.resto.enums.TypeAlerte;
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