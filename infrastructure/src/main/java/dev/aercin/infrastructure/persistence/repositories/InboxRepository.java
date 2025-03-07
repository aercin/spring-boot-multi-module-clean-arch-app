package dev.aercin.infrastructure.persistence.repositories;

import dev.aercin.domain.abstractions.IInboxRepository;
import dev.aercin.domain.entities.Inbox;
import dev.aercin.infrastructure.persistence.entities.InboxEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class InboxRepository implements IInboxRepository {

    private final JpaInboxRepository jpaInboxRepo;
    private final ModelMapper modelMapper;

    @Override
    public void save(Inbox e) {
        InboxEntity inboxEntity = this.modelMapper.map(e, InboxEntity.class);

        this.jpaInboxRepo.save(inboxEntity);
    }

    @Override
    public Optional<Inbox> findByIdAndConsumerType(UUID id, String consumerType) {

        Optional<InboxEntity> inboxEntityOpt = this.jpaInboxRepo.findByIdAndConsumerType(id, consumerType);

        if(inboxEntityOpt.isPresent())
        {
            return Optional.of(this.modelMapper.map(inboxEntityOpt.get(), Inbox.class));
        }

        return Optional.empty();
    }
}
