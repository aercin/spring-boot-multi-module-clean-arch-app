package dev.aercin.application.features.validate_token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResultData {
    private final Boolean isValid;
    private final String clientId;
}
