package br.com.rvigo.accessmanager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@OpenAPIDefinition(info = @Info(title = "Access Manager API", version = "1.0", description = "Access Manager"))
public class AccessManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccessManagerApplication.class, args);
    }
}
