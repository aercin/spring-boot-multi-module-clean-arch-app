package dev.aercin.domain.abstractions;

import dev.aercin.domain.entities.Inbox;

import java.util.Optional;
import java.util.UUID;

public interface IInboxRepository {
    void save(Inbox e);

    Optional<Inbox> findByIdAndConsumerType(UUID id, String consumerType);
}
