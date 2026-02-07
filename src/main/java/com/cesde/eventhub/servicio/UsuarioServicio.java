package com.cesde.eventhub.servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.UsuarioDTO;
import com.cesde.eventhub.enumeraciones.RolesUsuario;
import com.cesde.eventhub.modelos.Usuario;
import com.cesde.eventhub.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicio {

	@Autowired
	private  UsuarioRepositorio usuarioRepos;
	
	public UsuarioDTO crearCliente(UsuarioDTO usuarioDTO) {
		
		if(usuarioRepos.existsByEmail(usuarioDTO.getEmail())) {
		throw new RuntimeException("Ya existe un usuario con el email" + usuarioDTO.getEmail());
		
		}
		
		Usuario usuarioAGuardar = new Usuario();
		usuarioAGuardar.setNombre(usuarioDTO.getNombre());
		usuarioAGuardar.setEmail(usuarioDTO.getEmail());
		usuarioAGuardar.setContraseña(usuarioDTO.getContraseña());
        usuarioAGuardar.setRol(RolesUsuario.ClIENTE);		
        usuarioAGuardar.setActivo(true);
        
        Usuario usuarioGuardado = usuarioRepos.save(usuarioAGuardar);
        
		return UsuarioDTO.desdeEntidad(usuarioGuardado);
	}
}
