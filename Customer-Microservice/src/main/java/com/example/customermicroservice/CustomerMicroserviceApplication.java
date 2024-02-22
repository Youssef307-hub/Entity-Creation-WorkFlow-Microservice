package com.example.customermicroservice;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
		name = "Keycloak",
		openIdConnectUrl = "http://localhost:8090/realms/Entity-Creation-Work-Flow/.well-known/openid-configuration",
		scheme = "bearer",
		type = SecuritySchemeType.OPENIDCONNECT,
		in = SecuritySchemeIn.HEADER
)
public class CustomerMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerMicroserviceApplication.class, args);
	}

}
