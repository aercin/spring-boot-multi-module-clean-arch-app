package dev.aercin.application.features.order_success;

import dev.aercin.application.shared.mediator.Request;
import dev.aercin.application.shared.models.Result;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Command implements Request<Result> {
    private final UUID orderId;
}
