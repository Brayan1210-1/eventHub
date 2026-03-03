package com.cesde.eventhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.UserRoles;
import com.cesde.eventhub.repository.UserRepository;

@SpringBootApplication
public class EventhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventhubApplication.class, args);
	}
	
	 @Bean
	    public CommandLineRunner initData(UserRepository userRepository,
	                                      PasswordEncoder passwordEncoder) {
	        return args -> {
	        	
	        	String emailAdmin = "admin@gmail.com";
	        	
	        	if(userRepository.findByEmail(emailAdmin).isEmpty()) {
	        		
	            User admin = new User();
	            admin.setName("PrimerAdmin");
	            admin.setLastName("Quejada");
	            admin.setEmail(emailAdmin);
	            admin.setPhone("313672");
	            admin.setDocument("1038384953");
	            admin.setActive(true);
	            admin.setPassword(passwordEncoder.encode("Admin123"));
	            admin.setRoles(UserRoles.ADMIN);
	           
	            userRepository.save(admin);
	            System.out.println("El admin hace aparición");
	        	}
	        	
	        };
	 }

}
