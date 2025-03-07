package dev.aercin.presentation.configuration;

import dev.aercin.application.shared.mediator.Mediator;
import dev.aercin.presentation.middlewares.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Mediator mediator;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF devre dışı bırakılması
                .csrf(csrf -> csrf.disable())
                // Stateless session yönetimi
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Yetkilendirme ayarları: /auth/** endpointlerine izin, diğerleri için kimlik doğrulaması zorunlu
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Özel Bearer token doğrulama filter'ınızı ekleyin
                .addFilterBefore(new AuthenticationFilter(mediator), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
