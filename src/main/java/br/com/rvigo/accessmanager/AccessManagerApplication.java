package br.com.rvigo.accessmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude={UserDetailsServiceAutoConfiguration.class})
public class AccessManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessManagerApplication.class, args);
	}
}
