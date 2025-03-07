package dev.aercin.application.shared.integration_events.consume;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StockResponseEvent {
    private String messageId;
    private String[] messageType;
    private Message message;
}
