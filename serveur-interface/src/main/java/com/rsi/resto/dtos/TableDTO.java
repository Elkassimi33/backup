package com.rsi.resto.dtos;

import com.rsi.resto.enums.StatutTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO {
    private Long idTable;
    private Integer numero;
    private Integer capacite;
    private StatutTable statut;
}