package dev.aercin.infrastructure.persistence.entities;


import dev.aercin.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name="orders")
public class OrderEntity {
    @Id
    private UUID id;
    private UUID userId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JoinColumn(name = "orderId")
    private List<OrderProductEntity> products;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
