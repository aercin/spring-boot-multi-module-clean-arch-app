package dev.aercin.domain.entities;

import java.util.UUID;

public class Outbox {
    private UUID id;
    private String message;

    public Outbox() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
