package dev.aercin.domain.abstractions;

public interface IUnitOfWork {
   IOutboxRepository getOutboxRepo();
   IOrderRepository  getOrderRepo();
   IInboxRepository  getInboxRepo();

   void beginTran();
   void commit();
   void rollback();
}