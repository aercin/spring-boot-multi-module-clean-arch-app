package dev.aercin.application.features.order_place;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ResultData {
    private final UUID orderNo;
}
