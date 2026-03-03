package com.cesde.eventhub.service;
import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.dto.LoginDTO;
import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.UserRoles;
import com.cesde.eventhub.mapper.UserMapper;
import com.cesde.eventhub.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {


	private final UserRepository usuarioRepos;


	private final PasswordEncoder passwordEncoder;
	

	private final UserMapper usuarioMapper;
	
	

	@Transactional
	public UserResponseDTO crearCliente(UserRegisterDTO usuarioDTO) {

		if(usuarioRepos.existsByEmail(usuarioDTO.getEmail()) || usuarioRepos.existsByDocumento(usuarioDTO.getDocumento())) {
		throw new RuntimeException ("Ya existe un usuario con ese email o documento");

		}

		User usuarioAGuardar = usuarioMapper.haciaEntidad(usuarioDTO);
		
		usuarioAGuardar.setRol(UserRoles.CLIENTE);
		usuarioAGuardar.setActivo(true);

		//CIFRAR y asignar contraseña
        String contraseña = usuarioDTO.getContrasena();
        String contraseñaCifrada = passwordEncoder.encode(contraseña);
        usuarioAGuardar.setContrasena(contraseñaCifrada);

        User usuarioGuardado = usuarioRepos.save(usuarioAGuardar);

        //aún falta devolver el jwt
		return usuarioMapper.haciaDto(usuarioGuardado);
	}

	public UserResponseDTO iniciarSesion(LoginDTO login) {
		
		User usuarioEncontrado = findByEmail(login.getEmail());
		
		//mirar si la contraseña es correcta
		if(!passwordEncoder.matches(login.getContrasena(), usuarioEncontrado.getContrasena())) {
			throw new SecurityException("La contraseña es incorrecta");
		}
		
		UserResponseDTO usuarioRespuesta = usuarioMapper.haciaDto(usuarioEncontrado);
		
		return usuarioRespuesta;
	}

	public User findByEmail(String email) {
		
		Optional<User> usuarioABuscar = usuarioRepos.findByEmail(email);
		if(!usuarioABuscar.isPresent()) {
			throw new RuntimeException("No existe un usuario con ese email");
		}
		
		User usuario = usuarioABuscar.get();
		return usuario;
	}

}
