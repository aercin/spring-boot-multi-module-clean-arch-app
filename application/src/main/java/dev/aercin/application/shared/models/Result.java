package dev.aercin.application.shared.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Result {
    private final Boolean isSuccess;
    private final String[] errors;

    public static Result success(){
        return new Result(true, null);
    }

    public static Result fail(String... errors){
        return new Result(false, errors);
    }
}

