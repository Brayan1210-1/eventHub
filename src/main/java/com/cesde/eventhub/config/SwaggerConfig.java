package com.cesde.eventhub.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	  public OpenAPI usuarioApiOpenAPI() {
	    return new OpenAPI()
	      .info(new Info()
	        .title("EventHub - Documentación de uso")
	        .description("Todo lo que tienes que saber sobre EventHub")
	        .version("1.0.0")
	        .contact(new Contact()
	          .name("Soporte")
	          .email("bquebrada@cesde.net")
	          .url("https://cesde.edu.co")
	        )
	        .license(new License()
	          .name("MIT License")
	          .url("https://opensource.org/licenses/MIT")
	        )
	      )
	      .servers(List.of(
	        new Server()
	          .url("http://localhost:8081")
	          .description("Servidor local")
	        )
	      )
	      
	      .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
          .components(new io.swagger.v3.oas.models.Components()
              .addSecuritySchemes("BearerAuth", new SecurityScheme()
                  .name("BearerAuth")
                  .type(SecurityScheme.Type.HTTP)
                  .scheme("bearer")
                  .bearerFormat("JWT")));
	  }
}
