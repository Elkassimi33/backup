package com.elkassimi.shop.dtos.responseDtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.elkassimi.shop.model.enums.CommandStatus;

import lombok.Builder;

@Builder
public record CommandResponseDto(

        Long commandId,

        Long user,

        LocalDateTime dateCommand,

        LocalDateTime updatedAt,

        CommandStatus status,

        BigDecimal totalPrice,

        List<LigneCommandResponseDto> ligneCommands

) {
}
