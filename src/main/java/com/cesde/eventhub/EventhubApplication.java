package com.cesde.eventhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cesde.eventhub.enumeraciones.RolesUsuario;
import com.cesde.eventhub.modelos.Usuario;
import com.cesde.eventhub.repositorio.UsuarioRepositorio;

@SpringBootApplication
public class EventhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventhubApplication.class, args);
	}
	
	 @Bean
	    public CommandLineRunner initData(UsuarioRepositorio usuarioRepo,
	                                      PasswordEncoder passwordEncoder) {
	        return args -> {
	        	
	        	String emailAdmin = "admin@gmail.com";
	        	
	        	if(usuarioRepo.findByEmail(emailAdmin).isEmpty()) {
	        		
	            Usuario admin = new Usuario();
	            admin.setNombre("PrimerAdmin");
	            admin.setApellido("Quejada");
	            admin.setEmail(emailAdmin);
	            admin.setTelefono("313672");
	            admin.setDocumento("1038384953");
	            admin.setActivo(true);
	            admin.setContrasena(passwordEncoder.encode("Admin123"));
	            admin.setRol(RolesUsuario.ADMIN);
	           
	            usuarioRepo.save(admin);
	            System.out.println("El admin hace aparición");
	        	}
	        	
	        };
	 }

}
