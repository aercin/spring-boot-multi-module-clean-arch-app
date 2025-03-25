# spring-boot-multi-module-clean-arch-app
The programming language of order microservice in my noSqlExperiments repository was changed from .net to java

- Presentation Module is a Spring Boot Maven Project and other ones (domain, application and infrastructure) are vanilla maven project.
- Spring Web (spring-boot-starter-web)
- Spring Security (spring-boot-starter-security)
- Spring WebFlux (spring-boot-starter-webflux)
- Spring Boot Validation (spring-boot-starter-validation)
- Spring Cloud Stream (spring-cloud-starter-stream-rabbit); functional programming style
- Spring Boot Data Jpa (spring-boot-starter-data-jpa)
- Spring Boot Logging (spring-boot-starter-logging) 
 - Logback with File log 
- Global Exception Handler
- ModelMapper for object mapping
- Lombok
- Mediator pattern 
- UnitOfWork pattern
- Inbox/Outbox Pattern
- Object Result Pattern for consistent result for all ep responses
- Spring Boot Scheduling Feature for produce message to MessageQueue periodicly
- As alternatives to Spring WebFlux and Spring Cloud Stream, Spring Integration AMQP & HTTP
