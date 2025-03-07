package dev.aercin.domain.abstractions;

import dev.aercin.domain.entities.Order;

import java.util.Optional;
import java.util.UUID;

public interface IOrderRepository {
    void save(Order e);

    Optional<Order> findById(UUID id);
}
