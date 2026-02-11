package com.cesde.eventhub.servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.dto.UsuarioDTO;
import com.cesde.eventhub.enumeraciones.RolesUsuario;
import com.cesde.eventhub.modelos.Usuario;
import com.cesde.eventhub.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicio {

	@Autowired
	private  UsuarioRepositorio usuarioRepos;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public UsuarioDTO crearCliente(UsuarioDTO usuarioDTO) {
		
		if(usuarioRepos.existsByEmail(usuarioDTO.getEmail()) || usuarioRepos.existsByDocumento(usuarioDTO.getDocumento())) {
		throw new RuntimeException("Ya existe un usuario con ese email o documento");
		
		}
		
		Usuario usuarioAGuardar = new Usuario();
		usuarioAGuardar.setNombre(usuarioDTO.getNombre());
		usuarioAGuardar.setEmail(usuarioDTO.getEmail());
		usuarioAGuardar.setApellido(usuarioDTO.getApellido());
		usuarioAGuardar.setDocumento(usuarioDTO.getDocumento());
		usuarioAGuardar.setTelefono(usuarioDTO.getTelefono());
        usuarioAGuardar.setRol(RolesUsuario.CLIENTE);		
        usuarioAGuardar.setActivo(true);
        
        String contraseña = usuarioDTO.getContrasena();
        
        //CIFRAR CONTRASEÑA
        String contraseñaCifrada = passwordEncoder.encode(contraseña);
        
        usuarioAGuardar.setContrasena(contraseñaCifrada);
        
        Usuario usuarioGuardado = usuarioRepos.save(usuarioAGuardar);
        
		return UsuarioDTO.desdeEntidadCliente(usuarioGuardado);
	}
	
	
	
}
