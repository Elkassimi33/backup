package com.elkassimi.shop.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elkassimi.shop.model.Command;
import com.elkassimi.shop.model.enums.CommandStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {

    List<Command> findByUserUserId(Long userId);

    List<Command> findByStatus(CommandStatus status);

    List<Command> findByDateCommandBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Command> findByUserUserIdOrderByDateCommandDesc(Long userId);

    @Query("SELECT  sum(c.totalPrice) from Command c where c.status = :status")
    BigDecimal sumTotalPriceByStatus(@Param("status") CommandStatus status);

    Long countByStatus(CommandStatus status);

    List<Command> findByTotalPriceGreaterThan(BigDecimal minPrice);

    BigDecimal getAverageTotalPriceByStatus(CommandStatus livree);

    Optional<Command> findTopByUserUserIdOrderByDateCommandDesc(Long userId);

    Optional<Command> findTopByOrderByTotalPriceDesc();

}
