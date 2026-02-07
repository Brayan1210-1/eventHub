package com.cesde.eventhub.configuraciones;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class ConfiguracionSwagger {
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
	      .tags(List.of(
	        new Tag()
	          .name("Gestión de usuarios")
	          .description("administración básica de usuarios (leer,borrar,actualizar y crear) PARA ADMIN")
	      )
	    );
	  }
}
