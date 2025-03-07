package dev.aercin.presentation.controllers;

import dev.aercin.application.features.create_token.Command;
import dev.aercin.application.shared.mediator.Mediator;
import dev.aercin.application.shared.models.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final Mediator mediator;

    @PostMapping("/token")
    public ResponseEntity<Result> createToken(@RequestBody @Valid Command request){
        return ResponseEntity.ok(this.mediator.send(request));
    }
}
