package com.example.customermicroservice.utility;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Microservice")
                        .description("This microservice is the gateway to create your customers and it" +
                                " then send it to the workflow microservice so it can complete the customer creation")
                        .version("1.0.0"));
    }
}

