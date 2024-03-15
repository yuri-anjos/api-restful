package br.com.yuri.studies.restfulspringboot.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Api Restful Spring Boot 3")
                        .version("v1")
                        .description("Api Restful Spring Boot 3")
                        .termsOfService("")
                        .license(
                                new License()
                                        .name("")
                                        .url("")
                        )
                );
    }
}
