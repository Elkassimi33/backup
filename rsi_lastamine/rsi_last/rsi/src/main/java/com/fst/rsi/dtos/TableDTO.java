package com.fst.rsi.dtos;
import com.fst.rsi.model.StatutTable;
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