package com.elkassimi.shop.dtos.requestDtos;

import java.time.LocalDateTime;
import java.util.List;

public record CommandRequestDto(

        Long userId,

        LocalDateTime dateCommand,

        LocalDateTime updatedAt,

        List<LigneCommandRequestDto> ligneCommands) {
}
