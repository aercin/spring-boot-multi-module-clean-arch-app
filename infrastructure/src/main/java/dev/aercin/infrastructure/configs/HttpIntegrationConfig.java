package dev.aercin.infrastructure.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.integration.http.dsl.Http;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@IntegrationComponentScan(basePackages = "dev.aercin.infrastructure.services")
public class HttpIntegrationConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        // Bağlantı kurulurken maksimum bekleme süresi (milisaniye cinsinden)
        requestFactory.setConnectTimeout(2000);
        // Sunucudan yanıt bekleme süresi (milisaniye cinsinden)
        requestFactory.setReadTimeout(3000);
        return new RestTemplate(requestFactory);
    }

    @Bean
    public RequestHandlerRetryAdvice httpRetryAdvice() {
        RequestHandlerRetryAdvice advice = new RequestHandlerRetryAdvice();

        // RetryTemplate oluştur
        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        // Denemeler arası gecikme
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000); // 2 saniye bekleme
        retryTemplate.setBackOffPolicy(backOffPolicy);

        advice.setRetryTemplate(retryTemplate);

        // Tüm retry denemeleri başarısız olursa
        advice.setRecoveryCallback(context -> {
            Throwable lastThrowable = context.getLastThrowable();
            log.error("Tüm retry denemeleri tükendi. Hata: " + lastThrowable.getMessage());
            return null;
        });

        return advice;
    }

    @Bean
    public IntegrationFlow httpOutboundFlow(@Qualifier("httpRetryAdvice") RequestHandlerRetryAdvice retryAdvice, RestTemplate restTemplate) {
        return IntegrationFlow.from("httpOutboundChannel")
                .enrichHeaders(h -> h.header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .handle(Http.outboundGateway((m) -> (String) m.getHeaders().get("uri"))
                                .httpMethod(HttpMethod.GET)
                                .expectedResponseType(String.class)
                                // RestTemplate'in request factory'si timeout ayarlarını içerir.
                                .requestFactory(restTemplate.getRequestFactory()),
                        e -> e.advice(retryAdvice))
                // Bu flow, dönen yanıtı otomatik olarak reply mesajı olarak geri iletir.
                .get();
    }
}
