package com.elkassimi.shop.dtos.responseDtos;

import java.util.List;

public record CategoryResponseDto(

        Long categoryId,
        String name,
        String description,
        String imageUrl
) {
}
