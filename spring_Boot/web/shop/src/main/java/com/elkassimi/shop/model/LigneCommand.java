package com.elkassimi.shop.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ligne_command")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ligne_command_id")
    private Long ligneCommandId;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(0)
    private Long quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "command_id", nullable = false)
    private Command command;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceUnitaire;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sousTotal;

    @PrePersist
    @PreUpdate
    public void calculerSousTotal() {
        if (product.getDiscount() != 0.0) {
            this.priceUnitaire = priceUnitaire.subtract(BigDecimal.valueOf(product.getDiscount()));
        }
        if (priceUnitaire != null && quantity != null) {
            this.sousTotal = priceUnitaire.multiply(BigDecimal.valueOf(quantity));
        }
    }

}
