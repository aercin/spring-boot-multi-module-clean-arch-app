package dev.aercin.application.shared.integration_events.produce;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Message {
      private String orderId;
      private OrderItem[] items;
}
