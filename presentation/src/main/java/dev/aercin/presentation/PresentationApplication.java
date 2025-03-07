package dev.aercin.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "dev.aercin")
@EnableJpaRepositories(basePackages = "dev.aercin.infrastructure.persistence.repositories")
@EntityScan(basePackages = "dev.aercin.infrastructure.persistence.entities")
public class PresentationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PresentationApplication.class, args);
	}

}
