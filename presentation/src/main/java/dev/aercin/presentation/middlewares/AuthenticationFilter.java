package dev.aercin.presentation.middlewares;

import dev.aercin.application.features.validate_token.Command;
import dev.aercin.application.features.validate_token.ResultData;
import dev.aercin.application.shared.mediator.Mediator;
import dev.aercin.application.shared.models.PayloadResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final Mediator mediator;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // /api/v1/auth ile başlayan isteklerde filtreyi devre dışı bırakmak
        if (path.startsWith("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
            String token = authHeader.substring(7);

            PayloadResult<ResultData> validationResult = (PayloadResult<ResultData>)this.mediator.send(new Command(token));

            if(!validationResult.getData().getIsValid()){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid token");
            }

            // Doğrulama başarılı, Spring Context içerisine Authentication ekleniyor
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(validationResult.getData().getClientId(), null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or invalid");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
