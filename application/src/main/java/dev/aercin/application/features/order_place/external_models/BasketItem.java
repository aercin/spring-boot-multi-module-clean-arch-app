package dev.aercin.application.features.order_place.external_models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@Data
public class BasketItem {
    private UUID productId;
    private BigDecimal price;
    private Integer quantity;
}