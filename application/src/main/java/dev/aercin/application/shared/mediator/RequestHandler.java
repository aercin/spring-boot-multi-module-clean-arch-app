package dev.aercin.application.shared.mediator;

public interface RequestHandler<TRequest extends Request<TResponse>, TResponse> {
      TResponse handle(TRequest request);
}
