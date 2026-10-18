package com.rsi.resto.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeRequestDTO {

    private Long idPlat;
    private Integer quantite;

    private String options;
}
