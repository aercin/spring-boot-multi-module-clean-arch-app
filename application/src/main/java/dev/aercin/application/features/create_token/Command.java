package dev.aercin.application.features.create_token;

import dev.aercin.application.shared.mediator.Request;
import dev.aercin.application.shared.models.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Command implements Request<Result> {
    @NotBlank(message = "Client ID cannot be null or empty")
    private String clientId;

    @NotBlank(message = "Client Secret cannot be null or empty")
    private String clientSecret;
}
