package com.cesde.eventhub.servicio;


import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.modelos.Usuario;
import com.cesde.eventhub.repositorio.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    	
    	Usuario usuario;
    	
    	if (isUUID(identifier)) {
            usuario = usuarioRepositorio.findById(UUID.fromString(identifier))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + identifier));
        } else {
    	
        usuario = usuarioRepositorio.findByEmail(identifier)
        		.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
      
     }
    	
    	
    	return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getId().toString())
                .password(usuario.getContrasena())
                .roles(usuario.getRol().name())
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

