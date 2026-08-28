package com.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal de la aplicación Spring Boot.
 * Microservicio de Reservas de Hotel con Clean Architecture.
 */
@SpringBootApplication(scanBasePackages = "com.hotel")
@EntityScan(basePackages = "com.hotel.infrastructure.persistence.entity")
@EnableJpaRepositories(basePackages = "com.hotel.infrastructure.persistence.repository")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
