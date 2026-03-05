package com.cesde.eventhub.service;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.dto.LoginDTO;
import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.Role;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.UserRoles;
import com.cesde.eventhub.exception.custom.*;
import com.cesde.eventhub.exception.custom.InternalError;
import com.cesde.eventhub.mapper.ClientMapper;
import com.cesde.eventhub.mapper.UserMapper;
import com.cesde.eventhub.repository.ClientRepository;
import com.cesde.eventhub.repository.RoleRepository;
import com.cesde.eventhub.repository.UserRepository;
import com.cesde.eventhub.entity.Client;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {


	private final UserRepository userRepository;
	
	private final ClientRepository clientRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;
	
	private final UserMapper userMapper;
	
	private final ClientMapper clientMapper; 
	
	

	@Transactional
	public UserResponseDTO createClient(UserRegisterDTO userDTO) {
		
		validateData(userDTO);

		User userToSave = userMapper.haciaEntidad(userDTO);
		
		Role clientRole = roleRepository.findByNameRole(UserRoles.CLIENTE)
                .orElseThrow(() -> new InternalError("Error: El rol CLIENTE no está configurado en la base de datos."));
		
		userToSave.getRoles().add(clientRole);
		userToSave.setActive(true);

		//CIFRAR y asignar contraseña
        String password = userDTO.getPassword();
        String passwordEncrypted = passwordEncoder.encode(password);
        userToSave.setPassword(passwordEncrypted);

        User userSave = userRepository.save(userToSave);
        
        Client client = clientMapper.toEntity(userDTO);
        client.setUser(userSave);
        clientRepository.save(client);

		return userMapper.haciaDto(userSave);
	}

	public UserResponseDTO iniciarSesion(LoginDTO login) {
		
		User foundUser = findByEmail(login.getEmail());
		
		//mirar si la contraseña es correcta
		if(!passwordEncoder.matches(login.getPassword(), foundUser.getPassword())) {
			throw new NotMatch("La contraseña es incorrecta");
		}
		
		UserResponseDTO userResponse = userMapper.haciaDto(foundUser);
		
		return userResponse;
	}

	public void validateData(UserRegisterDTO userDTO) {
		
		if(clientRepository.existsByDocument(userDTO.getDocument())) {
			throw new InvalidRegistration("El usuario ya existe con ese documento");
		}
		
		if(userRepository.existsByEmail(userDTO.getEmail())) {
			throw new InvalidRegistration("El usuario ya existe con ese correo");
		}
		
		if(clientRepository.existsByPhone(userDTO.getPhone())) {
			throw new InvalidRegistration("Ya existe un usuario con ese teléfono");
		}
		
	}
	
	public User findByEmail(String email) {
		
		User userToSearch = userRepository.findByEmail(email)
				.orElseThrow(() -> new DataNotFound("No se encontró el usuario con ese email"));
		
		return userToSearch;
	}
	
	public User findById(UUID id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new DataNotFound("No existe un usuario con el id" ));
			return user;
	}

	

}
