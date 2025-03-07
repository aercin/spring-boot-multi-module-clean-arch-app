package dev.aercin.infrastructure.persistence.repositories;

import dev.aercin.infrastructure.persistence.entities.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOutboxRepository extends JpaRepository<OutboxEntity, UUID> {
}
