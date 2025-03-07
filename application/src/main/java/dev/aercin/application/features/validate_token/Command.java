package dev.aercin.application.features.validate_token;

import dev.aercin.application.shared.mediator.Request;
import dev.aercin.application.shared.models.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Command implements Request<Result> {
    @NotBlank(message = "Auth token cannot be null or empty")
    private final String token;
}
