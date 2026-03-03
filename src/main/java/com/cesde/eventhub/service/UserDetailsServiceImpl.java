package com.cesde.eventhub.service;


import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    	
    	User user;
    	
    	if (isUUID(identifier)) {
            user = userRepository.findById(UUID.fromString(identifier))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + identifier));
        } else {
    	
        user = userRepository.findByEmail(identifier)
        		.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
      
     }
    	
    	String[] rolesName = user.getRoles().stream()
    			.map(role -> role.getNameRole().name())
    			.toArray(String[]::new);
    	
    	
    	return org.springframework.security.core.userdetails.User
                .withUsername(user.getId().toString())
                .password(user.getPassword())
                .roles(rolesName)
                .build();
    }
    
    private boolean isUUID(String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    
}

