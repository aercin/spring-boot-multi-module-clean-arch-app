package dev.aercin.application.shared.models;

import lombok.Getter;

@Getter
public class PayloadResult<T> extends Result {
    private final T data;

    private PayloadResult(Boolean isSuccess, String[] errors, T data) {
        super(isSuccess, errors);
        this.data = data;
    }

    public static <T> Result success(T data) {
        return new PayloadResult<>(true, null, data);
    }
}
