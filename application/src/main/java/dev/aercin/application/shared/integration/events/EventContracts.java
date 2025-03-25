package dev.aercin.application.shared.integration.events;

public class EventContracts {
    public static final String stockNotDecreasedEventMessageType = "urn:message:core_messages.IntegrationEvents:StockNotDecreasedEvent";
    public static final String stockDecreasedEventMessageType = "urn:message:core_messages.IntegrationEvents:StockDecreasedEvent";
    public static final String orderPlacedEventMessageType ="urn:message:core_messages.IntegrationEvents:OrderPlacedEvent";
}
