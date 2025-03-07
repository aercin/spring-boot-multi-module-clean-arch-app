package dev.aercin.infrastructure.persistence.repositories;

import dev.aercin.domain.abstractions.IOrderRepository;
import dev.aercin.domain.entities.Order;
import dev.aercin.infrastructure.persistence.entities.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OrderRepository implements IOrderRepository {

    private final JpaOrderRepository jpaOrderRepo;
    private final ModelMapper modelMapper;

    @Override
    public void save(Order e) {

        OrderEntity orderEntity = this.modelMapper.map(e,OrderEntity.class);

        this.jpaOrderRepo.save(orderEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {

        Optional<OrderEntity> orderEntityOpt = this.jpaOrderRepo.findById(id);

        if(orderEntityOpt.isPresent())
        {
            return Optional.of(this.modelMapper.map(orderEntityOpt.get(),Order.class));
        }

        return Optional.empty();
    }
}
