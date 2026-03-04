package com.cesde.eventhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cesde.eventhub.entity.Role;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.UserRoles;
import com.cesde.eventhub.repository.UserRepository;
import com.cesde.eventhub.repository.RoleRepository;

@SpringBootApplication
public class EventhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventhubApplication.class, args);
	}
	
	 @Bean
	    public CommandLineRunner initData(UserRepository userRepository,
	                                      PasswordEncoder passwordEncoder, 
	                                      RoleRepository roleRepository) {
	       
		    return args -> {
		    	
		    	for (UserRoles roleName : UserRoles.values()) {
		            if (roleRepository.findByNameRole(roleName).isEmpty()) {
		                roleRepository.save(new Role(roleName));
		                System.out.println("Rol creado: " + roleName);
		            }
		        }
	        	
	        	String emailAdmin = "admin@gmail.com";
	        	
	        	if(userRepository.findByEmail(emailAdmin).isEmpty()) {
	        		
	            User admin = new User();
	           
	            admin.setEmail(emailAdmin);
	            admin.setActive(true);
	            admin.setPassword(passwordEncoder.encode("Admin123"));
	            
	            Optional<Role> roleOpt = roleRepository.findByNameRole(UserRoles.ADMIN);
	            Role role = roleOpt.get();
	            
	            admin.getRoles().add(role);
	            
	            Optional<Role> roleOpt2 = roleRepository.findByNameRole(UserRoles.ORGANIZADOR);
	            Role roleO = roleOpt2.get();
	            
	            admin.getRoles().add(roleO);
	           
	            userRepository.save(admin);
	            System.out.println("El admin hace aparición");
	        	}
	        	
	        };
	 }

}
