package dev.aercin.application.features.create_token;

import dev.aercin.application.shared.mediator.RequestHandler;
import dev.aercin.application.shared.models.PayloadResult;
import dev.aercin.application.shared.models.Result;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RequiredArgsConstructor
@Component("createAuthTokenCommandHandler")
public class CommandHandler implements RequestHandler<Command, Result> {

    @Value("${security.client.id}")
    private String validClientId;

    @Value("${security.client.secret}")
    private String validClientSecret;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Override
    public Result handle(Command request) {

        if(!validClientId.equals(request.getClientId())
             || !validClientSecret.equals(request.getClientSecret())){
             return Result.fail("Invalid credentials");
        }

        Key hmacKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        // Token üretimi: subject olarak clientId, 1 saat geçerlilik süresi
        String token = Jwts.builder()
                .setSubject(request.getClientId())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();

        return PayloadResult.success(new ResultData(token));
    }
}
