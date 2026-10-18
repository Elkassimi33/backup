package com.elkassimi.shop.dtos.requestDtos;

import java.math.BigDecimal;

public record ProductUpdateRequestDto(
        String name,
        BigDecimal price,
        Long quantity,
        String description,
        String categoryName,
        Double discount,
        Boolean isDesponible) {
}
