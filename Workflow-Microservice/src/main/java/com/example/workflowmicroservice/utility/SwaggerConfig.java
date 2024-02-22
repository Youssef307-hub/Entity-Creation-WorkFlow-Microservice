package com.example.workflowmicroservice.utility;

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
                        .title("Entity Creation Workflow Microservice")
                        .description("The creation of the entities is based on the role of the user who creates the entity, " +
                                "every entity has a predefined number of steps needed to create the entity and that is based on the entity type.")
                        .version("1.0.0"));
    }
}

