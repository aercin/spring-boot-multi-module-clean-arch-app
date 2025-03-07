package dev.aercin.application.features.order_place;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aercin.application.features.order_place.external_models.BasketResult;
import dev.aercin.application.shared.integration_events.produce.Message;
import dev.aercin.application.shared.integration_events.produce.OrderPlacedEvent;
import dev.aercin.application.shared.mediator.RequestHandler;
import dev.aercin.application.shared.models.PayloadResult;
import dev.aercin.application.shared.models.Result;
import dev.aercin.domain.abstractions.IUnitOfWork;
import dev.aercin.domain.entities.Order;
import dev.aercin.domain.entities.OrderProduct;
import dev.aercin.domain.entities.Outbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Slf4j //bu lombok annotation ile private static final Logger log örneği bizim adımıza oluşturuluyor.
@RequiredArgsConstructor
@Component("orderPlaceCommandHandler")
public class CommandHandler implements RequestHandler<Command, Result> {
    private final ObjectProvider<IUnitOfWork> unitOfWorkProvider;
    private final ModelMapper modelMapper;
    private final ObjectMapper jacksonMapper;
    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;
    @Value("${webclient.basket-svc.baseUrl}")
    private String basketSvcBaseUrl;

    @Override
    public Result handle(Command request) {

        webClient = webClientBuilder.baseUrl(basketSvcBaseUrl).build();

        BasketResult basketRes = webClient.get()
                .uri(String.format("?UserId=%s", request.getUserId()))
                .retrieve()
                .bodyToMono(BasketResult.class)
                .block();

        if (basketRes.getIsSuccess()) {

            IUnitOfWork uow = unitOfWorkProvider.getObject();

            try {

                uow.beginTran();

                Order order = new Order();
                order.setId(UUID.randomUUID());
                order.setUserId(request.getUserId());
                order.setProducts(this.modelMapper.map(basketRes.getData(), new TypeToken<List<OrderProduct>>() {
                }.getType()));

                uow.getOrderRepo().save(order);

                OrderPlacedEvent event = new OrderPlacedEvent();
                event.setMessage(this.modelMapper.map(order, Message.class));

                Outbox outboxMessage = new Outbox();
                outboxMessage.setId(UUID.randomUUID());
                outboxMessage.setMessage(this.jacksonMapper.writeValueAsString(event));

                uow.getOutboxRepo().save(outboxMessage);

                uow.commit();

                return PayloadResult.success(new ResultData(order.getId()));

            } catch (Exception ex) {
                log.error("unexpected error is occured.", ex);
                uow.rollback();
            }
        }

        return Result.fail(new String[]{"unexpected situation is occured"});
    }
}
