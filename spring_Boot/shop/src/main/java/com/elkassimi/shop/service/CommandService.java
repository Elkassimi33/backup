package com.elkassimi.shop.service;

import com.elkassimi.shop.dtos.requestDtos.CommandRequestDto;
import com.elkassimi.shop.dtos.requestDtos.LigneCommandRequestDto;
import com.elkassimi.shop.dtos.responseDtos.CommandResponseDto;
import com.elkassimi.shop.dtos.responseDtos.LigneCommandResponseDto;
import com.elkassimi.shop.model.enums.CommandStatus;
import com.elkassimi.shop.exception.*;
import com.elkassimi.shop.model.Command;
import com.elkassimi.shop.model.LigneCommand;
import com.elkassimi.shop.model.Product;
import com.elkassimi.shop.model.User;
import com.elkassimi.shop.repository.CommandRepository;
import com.elkassimi.shop.repository.LigneCommandRepository;
import com.elkassimi.shop.repository.ProductRepository;
import com.elkassimi.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.coyote.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommandService {

    private final CommandRepository commandRepository;
    private final LigneCommandRepository ligneCommandRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // ==================== CREATE ====================

    public CommandResponseDto createCommand(CommandRequestDto commandRequest) {
        log.info("Creating new command for user with id: {}", commandRequest.userId());

        Command command = Command.builder()
                .user(findUserOrThrow(commandRequest.userId()))
                .dateCommand(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(CommandStatus.ACCEPTED)
                .totalPrice(BigDecimal.ZERO)
                .build();

        Command savedCommand = commandRepository.save(command);

        // Process line commands
        BigDecimal totalPrice = BigDecimal.ZERO;

        if (commandRequest.ligneCommands() != null && !commandRequest.ligneCommands().isEmpty()) {
            for (LigneCommandRequestDto ligneRequest : commandRequest.ligneCommands()) {
                LigneCommand ligneCommand = processLigneCommand(ligneRequest, savedCommand);
                totalPrice = totalPrice.add(ligneCommand.getSousTotal());
                ligneCommandRepository.save(ligneCommand);
                savedCommand.getLigneCommands().add(ligneCommand);
            }
        } else {
            throw new EmptyLigneCommandException("Command must contain at least one product");
        }

        // Update total price
        savedCommand.setTotalPrice(totalPrice);
        Command finalCommand = commandRepository.save(savedCommand);

        log.info("Command created successfully with id: {}, total price: {}",
                finalCommand.getCommandId(), totalPrice);

        return toCommandResponse(finalCommand);
    }

    private LigneCommand processLigneCommand(LigneCommandRequestDto ligneRequest, Command command) {
        Product product = findProductOrThrow(ligneRequest.productId());

        if (!product.getIsDesponible()) {
            throw new ProductNotAvailableException("Product is not available: " + product.getName());
        }

        if (product.getQuantity() < ligneRequest.quantity()) {
            throw new InsufficientStockException(
                    String.format("Insufficient stock for product: %s. Available: %d, Requested: %d",
                            product.getName(), product.getQuantity(), ligneRequest.quantity()));
        }

        // Calculate price with discount
        BigDecimal priceUnitaire = calculatePriceWithDiscount(product);

        // Reduce product stock
        product.setQuantity(product.getQuantity() - ligneRequest.quantity());
        if (product.getQuantity() == 0) {
            product.setIsDesponible(false);
        }
        productRepository.save(product);

        // Calculate subtotal
        BigDecimal sousTotal = priceUnitaire.multiply(BigDecimal.valueOf(ligneRequest.quantity()));

        return LigneCommand.builder()
                .product(product)
                .quantity(ligneRequest.quantity())
                .command(command)
                .priceUnitaire(priceUnitaire)
                .sousTotal(sousTotal)
                .build();
    }

    // ==================== READ ====================

    public CommandResponseDto getCommandById(Long commandId) {
        log.info("Fetching command with id: {}", commandId);
        Command command = findCommandOrThrow(commandId);
        return toCommandResponse(command);
    }

    public List<CommandResponseDto> getCommandsByUser(Long userId) {
        log.info("Fetching commands for user: {}", userId);
        List<Command> commands = commandRepository.findByUserUserId(userId);

        if (commands.isEmpty()) {
            throw new NoCommandsFoundException("No commands found for user: " + userId);
        }

        return commands.stream()
                .map(this::toCommandResponse)
                .collect(Collectors.toList());
    }

    public List<CommandResponseDto> getCommandsByStatus(CommandStatus status) {
        log.info("Fetching commands with status: {}", status);
        List<Command> commands = commandRepository.findByStatus(status);

        if (commands.isEmpty()) {
            throw new NoCommandsFoundException("No commands found with status: " + status);
        }

        return commands.stream()
                .map(this::toCommandResponse)
                .collect(Collectors.toList());
    }

    public List<CommandResponseDto> getAllCommands() {
        log.info("Fetching all commands");
        List<Command> commands = commandRepository.findAll();

        if (commands.isEmpty()) {
            throw new NoCommandsFoundException("No commands found");
        }

        return commands.stream()
                .map(this::toCommandResponse)
                .collect(Collectors.toList());
    }

    public Page<CommandResponseDto> getAllCommandsPaginated(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching commands with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return commandRepository.findAll(pageable)
                .map(this::toCommandResponse);
    }

    public List<CommandResponseDto> getCommandsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching commands between dates: {} and {}", startDate, endDate);
        List<Command> commands = commandRepository.findByDateCommandBetween(startDate, endDate);

        if (commands.isEmpty()) {
            throw new NoCommandsFoundException("No commands found in the specified date range");
        }

        return commands.stream()
                .map(this::toCommandResponse)
                .collect(Collectors.toList());
    }

    public List<CommandResponseDto> getCommandsByTotalPriceGreaterThan(BigDecimal minPrice) {
        log.info("Fetching commands with total price greater than: {}", minPrice);
        List<Command> commands = commandRepository.findByTotalPriceGreaterThan(minPrice);

        if (commands.isEmpty()) {
            throw new NoCommandsFoundException("No commands found with total price greater than: " + minPrice);
        }

        return commands.stream()
                .map(this::toCommandResponse)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalRevenue() {
        log.info("Calculating total revenue");
        BigDecimal totalRevenue = commandRepository.sumTotalPriceByStatus(CommandStatus.LIVREE);
        return totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
    }

    public Long getCommandCountByStatus(CommandStatus status) {
        log.info("Counting commands with status: {}", status);
        return commandRepository.countByStatus(status);
    }

    // ==================== UPDATE ====================

    public CommandResponseDto updateCommandStatus(Long commandId, CommandStatus newStatus) {
        log.info("Updating command status for id: {} to: {}", commandId, newStatus);

        Command command = findCommandOrThrow(commandId);

        validateStatusTransition(command.getStatus(), newStatus);

        command.setStatus(newStatus);
        command.setUpdatedAt(LocalDateTime.now());

        // If command is cancelled, restore product stock
        if (newStatus == CommandStatus.ANNULEE && command.getStatus() != CommandStatus.ANNULEE) {
            restoreProductStock(command);
        }

        Command updatedCommand = commandRepository.save(command);
        log.info("Command status updated successfully for id: {}", commandId);

        return toCommandResponse(updatedCommand);
    }

    private void validateStatusTransition(CommandStatus currentStatus, CommandStatus newStatus) {
        if (currentStatus == CommandStatus.LIVREE) {
            throw new IllegalStateException("Cannot update status of a finished command");
        }

        if (currentStatus == CommandStatus.ANNULEE) {
            throw new IllegalStateException("Cannot update status of a cancelled command");
        }
    }

    private void restoreProductStock(Command command) {
        log.info("Restoring product stock for cancelled command: {}", command.getCommandId());

        for (LigneCommand ligneCommand : command.getLigneCommands()) {
            Product product = ligneCommand.getProduct();
            product.setQuantity(product.getQuantity() + ligneCommand.getQuantity());
            if (product.getQuantity() > 0) {
                product.setIsDesponible(true);
            }
            productRepository.save(product);
        }

        log.info("Stock restored for command: {}", command.getCommandId());
    }

    public CommandResponseDto addProductToCommand(Long commandId, LigneCommandRequestDto ligneRequest) {
        log.info("Adding product to command: {}", commandId);

        Command command = findCommandOrThrow(commandId);

        // Check if command can be modified
        if (command.getStatus() != CommandStatus.ACCEPTED) {
            throw new IllegalStateException("Cannot add products to command with status: " + command.getStatus());
        }

        boolean productExists = command.getLigneCommands().stream()
                .anyMatch(lc -> lc.getProduct().getProductId().equals(ligneRequest.productId()));

        if (productExists) {
            throw new DuplicateProductInCommandException(
                    "Product already exists in this command. Use update endpoint to modify quantity.");
        }

        LigneCommand ligneCommand = processLigneCommand(ligneRequest, command);
        ligneCommandRepository.save(ligneCommand);
        command.getLigneCommands().add(ligneCommand);

        BigDecimal newTotalPrice = command.getTotalPrice().add(ligneCommand.getSousTotal());
        command.setTotalPrice(newTotalPrice);
        command.setUpdatedAt(LocalDateTime.now());

        Command updatedCommand = commandRepository.save(command);
        log.info("Product added successfully to command: {}", commandId);

        return toCommandResponse(updatedCommand);
    }

    public CommandResponseDto updateProductQuantityInCommand(Long commandId, Long productId, Long newQuantity) {
        log.info("Updating product quantity in command: {}, product: {}, new quantity: {}",
                commandId, productId, newQuantity);

        Command command = findCommandOrThrow(commandId);

        if (command.getStatus() != CommandStatus.ACCEPTED) {
            throw new IllegalStateException("Cannot modify command with status: " + command.getStatus());
        }

        LigneCommand ligneCommand = findLigneCommandInCommand(command, productId);
        Product product = ligneCommand.getProduct();

        Long oldQuantity = ligneCommand.getQuantity();
        Long quantityDifference = newQuantity - oldQuantity;

        if (quantityDifference > 0) {
            if (product.getQuantity() < quantityDifference) {
                throw new InsufficientStockException(
                        String.format("Insufficient stock to add %d more items. Available: %d",
                                quantityDifference, product.getQuantity()));
            }
            product.setQuantity(product.getQuantity() - quantityDifference);
        } else if (quantityDifference < 0) {
            // Removing items - restore stock
            product.setQuantity(product.getQuantity() - quantityDifference);
        }

        if (product.getQuantity() == 0) {
            product.setIsDesponible(false);
        } else {
            product.setIsDesponible(true);
        }

        productRepository.save(product);

        // Update line command
        BigDecimal priceUnitaire = calculatePriceWithDiscount(product);
        BigDecimal newSousTotal = priceUnitaire.multiply(BigDecimal.valueOf(newQuantity));

        ligneCommand.setQuantity(newQuantity);
        ligneCommand.setPriceUnitaire(priceUnitaire);
        ligneCommand.setSousTotal(newSousTotal);
        ligneCommandRepository.save(ligneCommand);

        // Recalculate total command price
        BigDecimal newTotalPrice = command.getLigneCommands().stream()
                .map(LigneCommand::getSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        command.setTotalPrice(newTotalPrice);
        command.setUpdatedAt(LocalDateTime.now());

        Command updatedCommand = commandRepository.save(command);
        log.info("Product quantity updated successfully in command: {}", commandId);

        return toCommandResponse(updatedCommand);
    }

    public CommandResponseDto removeProductFromCommand(Long commandId, Long productId) {
        log.info("Removing product from command: {}, product: {}", commandId, productId);

        Command command = findCommandOrThrow(commandId);

        if (command.getStatus() != CommandStatus.ACCEPTED) {
            throw new IllegalStateException("Cannot remove products from command with status: " + command.getStatus());
        }

        LigneCommand ligneCommand = findLigneCommandInCommand(command, productId);

        // Restore product stock
        Product product = ligneCommand.getProduct();
        product.setQuantity(product.getQuantity() + ligneCommand.getQuantity());
        product.setIsDesponible(true);
        productRepository.save(product);

        // Remove line command
        command.getLigneCommands().remove(ligneCommand);
        ligneCommandRepository.delete(ligneCommand);

        // Recalculate total price
        BigDecimal newTotalPrice = command.getLigneCommands().stream()
                .map(LigneCommand::getSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        command.setTotalPrice(newTotalPrice);
        command.setUpdatedAt(LocalDateTime.now());

        Command updatedCommand = commandRepository.save(command);
        log.info("Product removed successfully from command: {}", commandId);

        return toCommandResponse(updatedCommand);
    }

    // ==================== DELETE ====================

    @Transactional
    public void deleteCommand(Long commandId) {
        log.info("Deleting command with id: {}", commandId);

        Command command = findCommandOrThrow(commandId);

        // Only allow deletion of cancelled or finished commands
        if (command.getStatus() == CommandStatus.ACCEPTED) {
            throw new IllegalStateException("Cannot delete an accepted command. Cancel it first.");
        }

        // Restore stock if command was finished (not necessary if cancelled since stock
        // already restored)
        if (command.getStatus() == CommandStatus.LIVREE) {
            restoreProductStock(command);
        }

        commandRepository.delete(command);
        log.info("Command deleted successfully with id: {}", commandId);
    }

    // ==================== BUSINESS LOGIC ====================

    public BigDecimal calculateAverageOrderValue() {
        log.info("Calculating average order value for finished commands");
        BigDecimal average = commandRepository.getAverageTotalPriceByStatus(CommandStatus.LIVREE);
        return average != null ? average : BigDecimal.ZERO;
    }

    public CommandResponseDto getMostRecentCommandByUser(Long userId) {
        log.info("Fetching most recent command for user: {}", userId);
        Command command = commandRepository.findTopByUserUserIdOrderByDateCommandDesc(userId)
                .orElseThrow(() -> new NoCommandsFoundException("No commands found for user: " + userId));
        return toCommandResponse(command);
    }

    public CommandResponseDto getHighestValueCommand() {
        log.info("Fetching highest value command");
        Command command = commandRepository.findTopByOrderByTotalPriceDesc()
                .orElseThrow(() -> new NoCommandsFoundException("No commands found"));
        return toCommandResponse(command);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Command findCommandOrThrow(Long commandId) {
        return commandRepository.findById(commandId)
                .orElseThrow(() -> new CommandNotFoundException("Command not found with id: " + commandId));
    }

    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    private LigneCommand findLigneCommandInCommand(Command command, Long productId) {
        return command.getLigneCommands().stream()
                .filter(lc -> lc.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in this command"));
    }

    private BigDecimal calculatePriceWithDiscount(Product product) {
        BigDecimal originalPrice = product.getPrice();
        Double discount = product.getDiscount();

        if (discount != null && discount > 0) {
            BigDecimal discountedAmount = originalPrice.multiply(BigDecimal.valueOf(discount));
            return originalPrice.subtract(discountedAmount);
        }

        return originalPrice;
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    private CommandResponseDto toCommandResponse(Command command) {
        List<LigneCommandResponseDto> ligneCommandDtos = command.getLigneCommands().stream()
                .map(this::toLigneCommandResponse)
                .collect(Collectors.toList());

        return CommandResponseDto.builder()
                .user(command.getUser().getUserId())
                .commandId(command.getCommandId())
                .status(command.getStatus())
                .totalPrice(command.getTotalPrice())
                .dateCommand(command.getDateCommand())
                .ligneCommands(ligneCommandDtos)
                .build();
    }

    private LigneCommandResponseDto toLigneCommandResponse(LigneCommand ligneCommand) {
        return new LigneCommandResponseDto(
                ligneCommand.getLigneCommandId(),
                ligneCommand.getProduct().getProductId(),
                ligneCommand.getQuantity(),
                ligneCommand.getPriceUnitaire(),
                ligneCommand.getSousTotal());
    }
}
