package dev.aercin.application.shared.integration_events.consume;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class Message {
    private UUID orderId;
}
