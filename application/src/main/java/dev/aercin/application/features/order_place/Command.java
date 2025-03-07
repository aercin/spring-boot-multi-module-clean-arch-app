package dev.aercin.application.features.order_place;

import dev.aercin.application.shared.mediator.Request;
import dev.aercin.application.shared.models.Result;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
public class Command implements Request<Result> {
    @NotNull(message = "User ID cannot be null")
    private  UUID userId;
}

