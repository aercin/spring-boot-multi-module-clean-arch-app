package dev.aercin.application.features.order_place;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aercin.application.features.order_place.external_models.BasketResult;
import dev.aercin.application.shared.integration.service.IntegrationService;
import dev.aercin.application.shared.integration.events.produce.Message;
import dev.aercin.application.shared.integration.events.produce.OrderPlacedEvent;
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

import java.util.List;
import java.util.UUID;

@Slf4j //bu lombok annotation ile private static final Logger log örneği bizim adımıza oluşturuluyor.
@RequiredArgsConstructor
@Component("orderPlaceCommandHandler")
public class CommandHandler implements RequestHandler<Command, Result> {
    private final ObjectProvider<IUnitOfWork> unitOfWorkProvider;
    private final ModelMapper modelMapper;
    private final ObjectMapper jacksonMapper;
    @Value("${webclient.basket-svc.baseUrl}")
    private String basketSvcBaseUrl;
    private final IntegrationService integrationService;

    @Override
    public Result handle(Command request) {

        BasketResult basketRes = getBasketDetail(request.getUserId());

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

    private BasketResult getBasketDetail(UUID userId) {

        String basketJsonRes = this.integrationService.makeHttpGetCall(String.format("%s?UserId=%s", basketSvcBaseUrl, userId));

        BasketResult basketRes = null;

        try {
            basketRes = this.jacksonMapper.readValue(basketJsonRes, BasketResult.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return basketRes;
    }
}
