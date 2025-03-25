package dev.aercin.infrastructure.services;

import dev.aercin.application.shared.integration.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IntegrationServiceImp implements IntegrationService {

    private final MessageGateway messagingGateway;

    @Override
    public String makeHttpGetCall(String uri) {
        return this.messagingGateway.makeHttpGetCall(uri);
    }

    @Override
    public void sendToQueue(String message) {
        this.messagingGateway.sendToQueue(MessageBuilder.withPayload(message).build());
    }
}
