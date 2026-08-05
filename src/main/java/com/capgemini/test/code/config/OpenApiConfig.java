package com.capgemini.test.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig
{
	@Bean
	OpenAPI customOpenAPI()
	{
		return new OpenAPI().info(new Info().title("Capgemini Test API").version("1.0").description("API para gestión de usuarios y salas").contact(new Contact().name("Javier Subias").email("javier.subias-minguez@capgemini.com")));
	}
}