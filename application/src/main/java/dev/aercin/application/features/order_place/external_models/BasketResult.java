package dev.aercin.application.features.order_place.external_models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class BasketResult {
    private Boolean isSuccess;
    private String[] errors;
    private List<BasketItem> data;
}
