package dev.aercin.application.shared.mediator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class Mediator {

    private final ApplicationContext applicationContext;

    public <TResponse> TResponse send(Request<TResponse> request) {
        // Mevcut context'ten ilgili handler'ı bul
        Map<String, RequestHandler> handlers = applicationContext.getBeansOfType(RequestHandler.class);

        for (RequestHandler handler : handlers.values()) {
            if (handler.getClass().getGenericInterfaces()[0].getTypeName().contains(request.getClass().getName())) {
                return (TResponse) handler.handle(request);
            }
        }
        throw new RuntimeException("Handler not found");
    }
}
