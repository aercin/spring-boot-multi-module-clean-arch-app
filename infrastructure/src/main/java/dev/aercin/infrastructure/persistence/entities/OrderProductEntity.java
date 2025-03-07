package dev.aercin.infrastructure.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name="orderProducts")
public class OrderProductEntity {
    @Id
    private UUID id;
    private BigDecimal price;
    private Integer quantity;
}
