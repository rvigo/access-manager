package br.com.rvigo.accessmanager.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SwaggerConfiguration {

    @Bean
    @Primary
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Access Manager")
                        .description("Access Manager API")
                        .version("v1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearer",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
