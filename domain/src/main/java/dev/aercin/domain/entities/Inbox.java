package dev.aercin.domain.entities;

import java.util.UUID;

public class Inbox {
    private UUID id;
    private String consumerType;

    public Inbox(){
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getConsumerType() {
        return this.consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }
}
