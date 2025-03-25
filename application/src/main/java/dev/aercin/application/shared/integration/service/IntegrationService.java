package dev.aercin.application.shared.integration.service;

public interface IntegrationService {
      String makeHttpGetCall(String uri);

      void sendToQueue(String message);
}