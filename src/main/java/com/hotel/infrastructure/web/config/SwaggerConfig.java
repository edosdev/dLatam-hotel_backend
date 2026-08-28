package com.hotel.infrastructure.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Springdoc OpenAPI para documentación de la API.
 * Solo se activa en el perfil de desarrollo.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI hotelOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Reservation API")
                        .description("Microservicio de Reservas de Hotel con Clean Architecture, "
                                + "Spring Boot, PostgreSQL y Docker.")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@hotel.com")
                                .url("https://github.com/edosdev/dLatam-hotel_backend"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
