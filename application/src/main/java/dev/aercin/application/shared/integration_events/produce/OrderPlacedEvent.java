package dev.aercin.application.shared.integration_events.produce;

import dev.aercin.application.shared.integration_events.EventContracts;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderPlacedEvent {
    private String messageId = UUID.randomUUID().toString();
    private String[] messageType = new String[] {EventContracts.orderPlacedEventMessageType};
    private Message message;
}
