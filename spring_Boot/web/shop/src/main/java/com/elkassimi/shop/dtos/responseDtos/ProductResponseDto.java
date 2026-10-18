package com.elkassimi.shop.dtos.responseDtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ProductResponseDto(

        @NonNull Long productId,

        @NotBlank String categoryName,

        @NotBlank String name,

        @NonNull @Min(0) BigDecimal price,

        @NonNull @Min(0) Long quantity,

        String description,

        @NonNull @Range(min = 0, max = 1) Double discount,

        @NonNull Boolean isDesponible,

        LocalDate createdAt,

        LocalDate updatedAt,

        String imageUrl

) {
}
