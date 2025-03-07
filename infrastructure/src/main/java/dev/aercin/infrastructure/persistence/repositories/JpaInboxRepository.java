package dev.aercin.infrastructure.persistence.repositories;

import dev.aercin.infrastructure.persistence.entities.InboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaInboxRepository extends JpaRepository<InboxEntity, UUID> {
    Optional<InboxEntity> findByIdAndConsumerType(UUID id, String consumerType);
}
