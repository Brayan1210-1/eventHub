package com.cesde.eventhub.service;
import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.dto.LoginDTO;
import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.Role;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.UserRoles;
import com.cesde.eventhub.mapper.UserMapper;
import com.cesde.eventhub.repository.RoleRepository;
import com.cesde.eventhub.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {


	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;
	
	private final UserMapper userMapper;
	
	

	@Transactional
	public UserResponseDTO createClient(UserRegisterDTO userDTO) {

		if(userRepository.existsByEmail(userDTO.getEmail()) || userRepository.existsByDocument(userDTO.getDocument())) {
		throw new RuntimeException ("Ya existe un usuario con ese email o documento");

		}

		User userToSave = userMapper.haciaEntidad(userDTO);
		
		Role clientRole = roleRepository.findByNameRole(UserRoles.CLIENTE)
                .orElseThrow(() -> new RuntimeException("Error: El rol CLIENTE no está configurado en la base de datos."));
		
		userToSave.getRoles().add(clientRole);
		userToSave.setActive(true);

		//CIFRAR y asignar contraseña
        String password = userDTO.getPassword();
        String passwordEncrypted = passwordEncoder.encode(password);
        userToSave.setPassword(passwordEncrypted);

        User userSave = userRepository.save(userToSave);

        //aún falta devolver el jwt
		return userMapper.haciaDto(userSave);
	}

	public UserResponseDTO iniciarSesion(LoginDTO login) {
		
		User foundUser = findByEmail(login.getEmail());
		
		//mirar si la contraseña es correcta
		if(!passwordEncoder.matches(login.getPassword(), foundUser.getPassword())) {
			throw new SecurityException("La contraseña es incorrecta");
		}
		
		UserResponseDTO userResponse = userMapper.haciaDto(foundUser);
		
		return userResponse;
	}

	public User findByEmail(String email) {
		
		Optional<User> userToSearch = userRepository.findByEmail(email);
		if(!userToSearch.isPresent()) {
			throw new RuntimeException("No existe un usuario con ese email");
		}
		
		User user = userToSearch.get();
		return user;
	}

}
