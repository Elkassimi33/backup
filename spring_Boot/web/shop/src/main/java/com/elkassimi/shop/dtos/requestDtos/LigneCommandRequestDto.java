package com.elkassimi.shop.dtos.requestDtos;

import jakarta.validation.constraints.Min;

public record LigneCommandRequestDto (

        Long productId,

        @Min(0) Long quantity

){
}
