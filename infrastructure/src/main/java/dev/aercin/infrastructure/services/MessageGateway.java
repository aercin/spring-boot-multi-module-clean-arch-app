package dev.aercin.infrastructure.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway
public interface MessageGateway {

    @Gateway(requestChannel = "httpOutboundChannel", payloadExpression = "''")
    String makeHttpGetCall(@Header("uri") String uri);

    @Gateway(requestChannel="amqpOutboundChannel")
    void sendToQueue(@Payload Message<String> message);
}
