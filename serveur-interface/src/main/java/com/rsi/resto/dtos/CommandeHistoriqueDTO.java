package com.rsi.resto.dtos;




import com.rsi.resto.enums.StatutCommande;
import com.rsi.resto.enums.TypeCommande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeHistoriqueDTO {
    private Long idCommande;
    private LocalDateTime dateCommande;
    private TypeCommande typeCommande;
    private StatutCommande statut;
    private BigDecimal montantTotal;
    private Integer nombrePlats;
}
