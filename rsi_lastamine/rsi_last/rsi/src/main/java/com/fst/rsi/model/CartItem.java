package com.fst.rsi.model;

import com.fst.rsi.dtos.PlatDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private PlatDTO plat;
    private Integer quantity;
    private String options;

    public CartItem(PlatDTO plat) {
        this.plat = plat;
        this.quantity = 1;
        this.options = "";
    }

    public BigDecimal getSousTotal() {
        return plat.getPrix().multiply(BigDecimal.valueOf(quantity));
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }
}