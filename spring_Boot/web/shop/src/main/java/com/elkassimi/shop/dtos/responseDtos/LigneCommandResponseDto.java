package com.elkassimi.shop.dtos.responseDtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;

public record LigneCommandResponseDto(

        Long ligneCommandId,

        Long productId,

        @Min(0) Long quantity,

        BigDecimal priceUnitaire,

        BigDecimal sousTotal

) {
}
