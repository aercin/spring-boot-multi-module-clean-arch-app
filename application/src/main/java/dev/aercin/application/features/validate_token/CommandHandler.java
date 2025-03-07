package dev.aercin.application.features.validate_token;

import dev.aercin.application.shared.mediator.RequestHandler;
import dev.aercin.application.shared.models.PayloadResult;
import dev.aercin.application.shared.models.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component("validateTokenCommandHandler")
public class CommandHandler implements RequestHandler<Command, Result> {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Override
    public Result handle(Command request) {
        try {

            Key hmacKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Claims claims =Jwts.parser()
                               .setSigningKey(hmacKey)
                               .build()
                               .parseClaimsJws(request.getToken())
                               .getBody();

            Date expiration = claims.getExpiration();

            if (expiration == null || expiration.before(new Date())) {
                return PayloadResult.success(new ResultData(false, null));
            }

            return PayloadResult.success(new ResultData(true, claims.getSubject()));

        } catch (SignatureException | IllegalArgumentException e) {
            // İmza hatalı veya token parse edilemediğinde
            return PayloadResult.success(new ResultData(false, null));
        }
    }
}
