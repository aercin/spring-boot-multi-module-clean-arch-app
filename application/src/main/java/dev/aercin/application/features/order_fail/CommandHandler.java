package dev.aercin.application.features.order_fail;

import dev.aercin.application.shared.mediator.RequestHandler;
import dev.aercin.application.shared.models.Result;
import dev.aercin.domain.abstractions.IUnitOfWork;
import dev.aercin.domain.entities.Inbox;
import dev.aercin.domain.entities.Order;
import dev.aercin.domain.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component("orderFailCommandHandler")
public class CommandHandler implements RequestHandler<Command, Result> {

    private final ObjectProvider<IUnitOfWork> unitOfWorkProvider;

    @Override
    public Result handle(Command request) {
        IUnitOfWork uow = this.unitOfWorkProvider.getObject();
        if(uow.getInboxRepo().findByIdAndConsumerType(request.getOrderId(),"orderFailCommandHandler").isPresent())
        {
            return Result.success();
        }

        Optional<Order> orderOpt = uow.getOrderRepo().findById(request.getOrderId());
        if(orderOpt.isPresent()){
            try{
                uow.beginTran();

                Order order = orderOpt.get();
                order.setStatus(OrderStatus.Failed);
                uow.getOrderRepo().save(order);

                Inbox inboxMsg = new Inbox();
                inboxMsg.setId(order.getId());
                inboxMsg.setConsumerType("orderFailCommandHandler");
                uow.getInboxRepo().save(inboxMsg);

                uow.commit();

            } catch (Exception ex) {
                log.error(String.format("orderNo: %s, unexpected error", request.getOrderId()), ex);
                uow.rollback();
            }
        }else {
            log.error(String.format("orderNo: %s, order not found", request.getOrderId()));
        }

        return Result.success();
    }
}
