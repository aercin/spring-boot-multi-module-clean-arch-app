package dev.aercin.presentation.middlewares;

import dev.aercin.application.shared.models.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.toList());

        return ResponseEntity.ok(Result.fail(errors.toArray(new String[0])));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleGlobalExceptions(Exception ex) {
        log.error("An unexpected error occurred", ex);
        return ResponseEntity.ok(Result.fail("An unexpected error occurred: " + ex.getMessage()));
    }
}
