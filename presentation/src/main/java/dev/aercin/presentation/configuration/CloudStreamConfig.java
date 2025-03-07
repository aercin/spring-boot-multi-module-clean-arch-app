package dev.aercin.presentation.configuration;

import dev.aercin.application.shared.integration_events.EventContracts;
import dev.aercin.application.shared.integration_events.consume.StockResponseEvent;
import dev.aercin.application.shared.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
public class CloudStreamConfig {

    private final Mediator mediator;

    @Bean
    public Consumer<Message<StockResponseEvent>> consumeMessage(){
        return (msg) -> {
            StockResponseEvent stockResponse = msg.getPayload();
            if(Arrays.stream(stockResponse.getMessageType()).anyMatch(type -> type.equals(EventContracts.stockDecreasedEventMessageType))){
                this.mediator.send(new dev.aercin.application.features.order_success.Command(stockResponse.getMessage().getOrderId()));
            }
            else{
                this.mediator.send(new dev.aercin.application.features.order_fail.Command(stockResponse.getMessage().getOrderId()));
            }
        };
    }

    @Scheduled(fixedDelayString="${scheduling.produceMessage.fixedDelay}")
    public void produceMessage() {
        this.mediator.send(new dev.aercin.application.features.message_dispatch.Command());
    }
}
