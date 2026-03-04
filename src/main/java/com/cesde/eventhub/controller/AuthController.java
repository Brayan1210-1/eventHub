package com.cesde.eventhub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.LoginDTO;
import com.cesde.eventhub.dto.request.RequestTokenRefresh;
import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.ResponseLoginDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.RefreshToken;
import com.cesde.eventhub.service.JwtService;
import com.cesde.eventhub.service.RefreshTokenService;
import com.cesde.eventhub.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/autenticacion")
public class AuthController {

	
	private final UserService userService;


	private final JwtService jwtService;


	private final RefreshTokenService refreshTokenService;

	@PostMapping("/registro")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserRegisterDTO user) {
		
			UserResponseDTO userCreate = userService.createClient(user);

			String token = jwtService.generateAccessToken(userCreate.getId(), userCreate.getEmail(),
					userCreate.getRoles());

			return ResponseEntity.status(HttpStatus.CREATED).body(token);

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO login) {

			UserResponseDTO user = userService.iniciarSesion(login);

	    String accessToken = jwtService.generateAccessToken(
	    		user.getId(), 
			user.getEmail(), 
			user.getRoles());

			RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseLoginDTO(refreshToken.getToken(), accessToken));

	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshToken(@Valid @RequestBody RequestTokenRefresh token) {
		try {
			ResponseLoginDTO newTokens = refreshTokenService.renovateAccessToken(token.getTokenRefresh());

			return ResponseEntity.status(HttpStatus.OK).body(newTokens);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

}
