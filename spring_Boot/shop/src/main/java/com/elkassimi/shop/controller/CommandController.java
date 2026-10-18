package com.elkassimi.shop.controller;

import com.elkassimi.shop.dtos.requestDtos.CommandRequestDto;
import com.elkassimi.shop.dtos.requestDtos.LigneCommandRequestDto;
import com.elkassimi.shop.dtos.responseDtos.CommandResponseDto;
import com.elkassimi.shop.model.enums.CommandStatus;
import com.elkassimi.shop.service.CommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/commands")
@RequiredArgsConstructor
public class CommandController {

    private final CommandService commandService;

    @PostMapping
    public ResponseEntity<CommandResponseDto> createCommand(@Valid @RequestBody CommandRequestDto commandRequest) {
        log.info("REST request to create command for user with id: {}", commandRequest.userId());
        CommandResponseDto response = commandService.createCommand(commandRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandResponseDto> getCommandById(@PathVariable Long id) {
        log.info("REST request to get command by id: {}", id);
        CommandResponseDto response = commandService.getCommandById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommandResponseDto>> getCommandsByUser(@PathVariable Long userId) {
        log.info("REST request to get commands for user: {}", userId);
        List<CommandResponseDto> response = commandService.getCommandsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CommandResponseDto>> getCommandsByStatus(@PathVariable CommandStatus status) {
        log.info("REST request to get commands by status: {}", status);
        List<CommandResponseDto> response = commandService.getCommandsByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CommandResponseDto>> getAllCommands() {
        log.info("REST request to get all commands");
        List<CommandResponseDto> response = commandService.getAllCommands();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<CommandResponseDto>> getAllCommandsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "commandId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("REST request to get commands paginated");
        Page<CommandResponseDto> response = commandService.getAllCommandsPaginated(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CommandResponseDto>> getCommandsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        log.info("REST request to get commands by date range: {} to {}", startDate, endDate);
        List<CommandResponseDto> response = commandService.getCommandsByDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/min-price")
    public ResponseEntity<List<CommandResponseDto>> getCommandsByTotalPriceGreaterThan(
            @RequestParam BigDecimal minPrice) {
        log.info("REST request to get commands with total price > {}", minPrice);
        List<CommandResponseDto> response = commandService.getCommandsByTotalPriceGreaterThan(minPrice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/revenue/total")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        log.info("REST request to get total revenue");
        BigDecimal revenue = commandService.getTotalRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getCommandCountByStatus(@PathVariable CommandStatus status) {
        log.info("REST request to get command count by status: {}", status);
        Long count = commandService.getCommandCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-order-value")
    public ResponseEntity<BigDecimal> calculateAverageOrderValue() {
        log.info("REST request to calculate average order value");
        BigDecimal average = commandService.calculateAverageOrderValue();
        return ResponseEntity.ok(average);
    }

    @GetMapping("/user/{userId}/most-recent")
    public ResponseEntity<CommandResponseDto> getMostRecentCommandByUser(@PathVariable Long userId) {
        log.info("REST request to get most recent command for user: {}", userId);
        CommandResponseDto response = commandService.getMostRecentCommandByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/highest-value")
    public ResponseEntity<CommandResponseDto> getHighestValueCommand() {
        log.info("REST request to get highest value command");
        CommandResponseDto response = commandService.getHighestValueCommand();
        return ResponseEntity.ok(response);
    }

    // ==================== UPDATE ====================

    @PatchMapping("/{id}/status")
    public ResponseEntity<CommandResponseDto> updateCommandStatus(
            @PathVariable Long id,
            @RequestParam CommandStatus status) {
        log.info("REST request to update command status: {} -> {}", id, status);
        CommandResponseDto response = commandService.updateCommandStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/products")
    public ResponseEntity<CommandResponseDto> addProductToCommand(
            @PathVariable Long id,
            @Valid @RequestBody LigneCommandRequestDto ligneRequest) {
        log.info("REST request to add product to command: {}", id);
        CommandResponseDto response = commandService.addProductToCommand(id, ligneRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{commandId}/products/{productId}")
    public ResponseEntity<CommandResponseDto> updateProductQuantityInCommand(
            @PathVariable Long commandId,
            @PathVariable Long productId,
            @RequestParam Long quantity) {
        log.info("REST request to update product quantity in command: {}, product: {}, quantity: {}",
                commandId, productId, quantity);
        CommandResponseDto response = commandService.updateProductQuantityInCommand(commandId, productId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commandId}/products/{productId}")
    public ResponseEntity<CommandResponseDto> removeProductFromCommand(
            @PathVariable Long commandId,
            @PathVariable Long productId) {
        log.info("REST request to remove product from command: {}, product: {}", commandId, productId);
        CommandResponseDto response = commandService.removeProductFromCommand(commandId, productId);
        return ResponseEntity.ok(response);
    }

    // ==================== DELETE ====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        log.info("REST request to delete command: {}", id);
        commandService.deleteCommand(id);
        return ResponseEntity.noContent().build();
    }
}
