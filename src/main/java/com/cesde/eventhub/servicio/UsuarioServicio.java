package com.cesde.eventhub.servicio;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.dto.LoginDTO;
import com.cesde.eventhub.dto.UsuarioDTO;
import com.cesde.eventhub.dto.UsuarioRespuestaDTO;
import com.cesde.eventhub.enumeraciones.RolesUsuario;
import com.cesde.eventhub.mapper.UsuarioMapper;
import com.cesde.eventhub.modelos.Usuario;
import com.cesde.eventhub.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicio {

	@Autowired
	private  UsuarioRepositorio usuarioRepos;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UsuarioMapper usuarioMapper;

	@Transactional
	public UsuarioRespuestaDTO crearCliente(UsuarioDTO usuarioDTO) {

		if(usuarioRepos.existsByEmail(usuarioDTO.getEmail()) || usuarioRepos.existsByDocumento(usuarioDTO.getDocumento())) {
		throw new RuntimeException ("Ya existe un usuario con ese email o documento");

		}

		Usuario usuarioAGuardar = usuarioMapper.haciaEntidad(usuarioDTO);
		
		usuarioAGuardar.setRol(RolesUsuario.CLIENTE);
		usuarioAGuardar.setActivo(true);

		//CIFRAR y asignar contraseña
        String contraseña = usuarioDTO.getContrasena();
        String contraseñaCifrada = passwordEncoder.encode(contraseña);
        usuarioAGuardar.setContrasena(contraseñaCifrada);

        Usuario usuarioGuardado = usuarioRepos.save(usuarioAGuardar);

        //aún falta devolver el jwt
		return usuarioMapper.haciaDto(usuarioGuardado);
	}

	public UsuarioRespuestaDTO iniciarSesion(LoginDTO login) {
		Optional<Usuario> usuarioOpcional = usuarioRepos.findByEmail(login.getEmail());  
		
		//Verificar que el usuario exista
		if (!usuarioOpcional.isPresent()) {
			throw new RuntimeException("No existe un usuario con ese email");
		}
		
			
		Usuario usuario = usuarioOpcional.get();
		//mirar si la contraseña es correcta
		if(!passwordEncoder.matches(login.getContrasena(), usuario.getContrasena())) {
			throw new SecurityException("La contraseña es incorrecta");
		}
		
		UsuarioRespuestaDTO usuarioRespuesta = usuarioMapper.haciaDto(usuario);
		
		return usuarioRespuesta;
	}


}
