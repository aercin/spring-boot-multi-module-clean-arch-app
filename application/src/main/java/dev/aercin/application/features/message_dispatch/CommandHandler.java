package dev.aercin.application.features.message_dispatch;

import dev.aercin.application.shared.integration.service.IntegrationService;
import dev.aercin.application.shared.mediator.RequestHandler;
import dev.aercin.application.shared.models.Result;
import dev.aercin.domain.abstractions.IUnitOfWork;
import dev.aercin.domain.entities.Outbox;
import dev.aercin.domain.entities.Pageable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component("messageDispatchCommandHandler")
public class CommandHandler implements RequestHandler<Command, Result> {

    private final ObjectProvider<IUnitOfWork> uowObjectProvider;
    private final IntegrationService integrationService;

    @Override
    public Result handle(Command request) {

        IUnitOfWork uow = this.uowObjectProvider.getObject();

        List<Outbox> outboxList = uow.getOutboxRepo().find(Pageable.init(1, 10));

        for (Outbox outboxMsg : outboxList) {

            this.integrationService.sendToQueue(outboxMsg.getMessage());

//            try {
//                uow.getOutboxRepo().deleteById(outboxMsg.getId());
//            } catch (Exception ex) {
//                log.error(String.format("%s idli outbox message silinemedi", outboxMsg.getId()), ex);
//            }
        }

        return Result.success();
    }
}
