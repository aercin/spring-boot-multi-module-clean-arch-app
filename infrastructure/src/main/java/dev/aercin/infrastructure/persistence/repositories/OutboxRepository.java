package dev.aercin.infrastructure.persistence.repositories;

import dev.aercin.domain.abstractions.IOutboxRepository;
import dev.aercin.domain.entities.Outbox;
import dev.aercin.domain.entities.Pageable;
import dev.aercin.infrastructure.persistence.entities.OutboxEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OutboxRepository implements IOutboxRepository {

    private final JpaOutboxRepository jpaOutboxRepo;
    private final ModelMapper modelMapper;

    @Override
    public void save(Outbox e) {

        OutboxEntity outboxEntity = this.modelMapper.map(e, OutboxEntity.class);

        this.jpaOutboxRepo.save(outboxEntity);
    }

    @Override
    public void deleteById(UUID id) {

        this.jpaOutboxRepo.deleteById(id);
    }

    @Override
    public List<Outbox> find(Pageable pagingPrm) {

        org.springframework.data.domain.Pageable pageable = PageRequest.of(pagingPrm.getPageNo() - 1, pagingPrm.getPageSize(), Sort.by("id").ascending());

        List<OutboxEntity> outboxEntityList = this.jpaOutboxRepo.findAll(pageable).getContent();

        Type destinationListType = new TypeToken<List<Outbox>>() {
        }.getType();

        return this.modelMapper.map(outboxEntityList, destinationListType);
    }
}
