package dev.aercin.domain.abstractions;

import dev.aercin.domain.entities.Outbox;
import dev.aercin.domain.entities.Pageable;

import java.util.List;
import java.util.UUID;

public interface IOutboxRepository  {
    void save(Outbox e);

    void deleteById(UUID id);

    List<Outbox> find(Pageable pageable);
}
