package dev.aercin.infrastructure.configs;

import dev.aercin.application.shared.integration.events.EventContracts;
import dev.aercin.application.shared.integration.events.consume.StockResponseEvent;
import dev.aercin.application.shared.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.messaging.Message;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
@Configuration
@IntegrationComponentScan(basePackages = "dev.aercin.infrastructure.services")
public class AmqpIntegrationConfig {

    private final Mediator mediator;

    @Bean
    public Queue consumeQueue() {
        return new Queue("StockResponses", true);
    }

    @Bean
    public Queue produceQueue() {
        return new Queue("PlacedOrders", true);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue("StockResponses.error", true);
    }

    @Bean
    public RequestHandlerRetryAdvice amqpRetryAdvice(RabbitTemplate rabbitTemplate) {
        RequestHandlerRetryAdvice advice = new RequestHandlerRetryAdvice();

        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // Maksimum 3 deneme

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000); // Her retry arasında 2 saniye gecikme

        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        advice.setRetryTemplate(retryTemplate);
        advice.setRecoveryCallback(context -> {
            // Tüm retry denemeleri başarısız olursa bu callback çalışır.
            Message<?> failedMessage = (Message<?>) context.getAttribute("message");

            rabbitTemplate.convertAndSend("StockResponses.error", failedMessage.getPayload());

            log.error("Retry'ler tükendi. Hata alinan mesaj: " + failedMessage);

            return null;
        });

        return advice;
    }

    @Bean
    public IntegrationFlow amqpOutboundFlow(ConnectionFactory connectionFactory) {
        return IntegrationFlow.from("amqpOutboundChannel")
                .handle(Amqp.outboundAdapter(new RabbitTemplate(connectionFactory))
                        .routingKey("PlacedOrders"))
                .get();
    }

    @Bean
    public IntegrationFlow amqpInboundFlow(ConnectionFactory connectionFactory, @Qualifier("amqpRetryAdvice") RequestHandlerRetryAdvice retryAdvice) {
        return IntegrationFlow.from(
                        Amqp.inboundAdapter(connectionFactory, "StockResponses")
                )
                // byte[] -> StockResponseEvent (JSON parse)
                .transform(Transformers.fromJson(StockResponseEvent.class))
                .handle(message -> {
                            StockResponseEvent stockResponse = (StockResponseEvent) message.getPayload();
                            if (Arrays.stream(stockResponse.getMessageType()).anyMatch(type -> type.equals(EventContracts.stockDecreasedEventMessageType))) {
                                this.mediator.send(new dev.aercin.application.features.order_success.Command(stockResponse.getMessage().getOrderId()));
                            } else {
                                this.mediator.send(new dev.aercin.application.features.order_fail.Command(stockResponse.getMessage().getOrderId()));
                            }
                            // throw new RuntimeException("patlıyoruz");
                        },
                        e -> e.advice(retryAdvice))
                .get();
    }

    @Scheduled(fixedDelayString = "${scheduling.produceMessage.fixedDelay}")
    public void produceMessage() {
        this.mediator.send(new dev.aercin.application.features.message_dispatch.Command());
    }
}
