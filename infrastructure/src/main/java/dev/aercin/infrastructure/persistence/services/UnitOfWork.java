package dev.aercin.infrastructure.persistence.services;

import dev.aercin.domain.abstractions.IInboxRepository;
import dev.aercin.domain.abstractions.IOrderRepository;
import dev.aercin.domain.abstractions.IOutboxRepository;
import dev.aercin.domain.abstractions.IUnitOfWork;
import dev.aercin.infrastructure.persistence.repositories.InboxRepository;
import dev.aercin.infrastructure.persistence.repositories.OrderRepository;
import dev.aercin.infrastructure.persistence.repositories.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RequiredArgsConstructor
@Component
@Scope("prototype")
public class UnitOfWork implements IUnitOfWork {
    private final ApplicationContext applicationContext;
    private final PlatformTransactionManager transactionManager;
    private TransactionStatus transactionStatus;

    @Override
    public IOutboxRepository getOutboxRepo() {
        return this.applicationContext.getBean(OutboxRepository.class);
    }

    @Override
    public IOrderRepository getOrderRepo() {
        return this.applicationContext.getBean(OrderRepository.class);
    }

    @Override
    public IInboxRepository getInboxRepo() {
        return this.applicationContext.getBean(InboxRepository.class);
    }

    @Override
    public void beginTran() {
        if (this.transactionStatus == null) {
            this.transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        }
    }

    @Override
    public void commit() {
        if (this.transactionStatus != null) {
            this.transactionManager.commit(transactionStatus);
            this.transactionStatus = null;
        }
    }

    @Override
    public void rollback() {
        if (this.transactionStatus != null) {
            this.transactionManager.rollback(transactionStatus);
            this.transactionStatus = null;
        }
    }
}
