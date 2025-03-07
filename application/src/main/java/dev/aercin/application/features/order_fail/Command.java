package dev.aercin.application.features.order_fail;

import dev.aercin.application.shared.mediator.Request;
import dev.aercin.application.shared.models.Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Command implements Request<Result> {
    private final UUID orderId;
}
