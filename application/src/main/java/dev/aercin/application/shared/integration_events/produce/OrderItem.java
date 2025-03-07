package dev.aercin.application.shared.integration_events.produce;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderItem {
    private UUID productId;
    private Integer quantity;
}
