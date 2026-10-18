package com.elkassimi.shop.dtos.requestDtos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Range;
import org.springframework.lang.NonNull;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductRequestDto(

        @NotBlank String categoryName,

        @NotBlank String name,

        @NonNull @Min(0) BigDecimal price,

        // new quqntity to add it to stock
        @NonNull @Min(0) Long newQuantity,

        String description,

        @NonNull @Range(min = 0, max = 1) Double discount,

        @NonNull Boolean isDesponible

) {
}
