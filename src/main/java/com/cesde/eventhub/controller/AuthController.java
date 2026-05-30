package com.cesde.eventhub.controller;

import java.util.Map; 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.LoginDTO;
import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.ResponseLoginDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.RefreshToken;
import com.cesde.eventhub.service.JwtService;
import com.cesde.eventhub.service.RefreshTokenService;
import com.cesde.eventhub.service.UserService;
import com.cesde.eventhub.utils.*;


import jakarta.servlet.http.HttpServletResponse;
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
	public ResponseEntity<ResponseLoginDTO> createUser(@Valid @RequestBody UserRegisterDTO user) {
		
			UserResponseDTO userCreate = userService.createClient(user);

			String token = jwtService.generateAccessToken(userCreate.getId(), userCreate.getEmail(),
					userCreate.getRoles());
			
			ResponseLoginDTO response = new ResponseLoginDTO(token); 

			return ResponseEntity.status(HttpStatus.CREATED).body(response);

	}

	@PostMapping("/login")
	public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody LoginDTO login,  HttpServletResponse response) {

			UserResponseDTO user = userService.iniciarSesion(login);

	    String accessToken = jwtService.generateAccessToken(
	    		user.getId(), 
			user.getEmail(), 
			user.getRoles());

			RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
			
			
			CreateCookie.setRefreshTokenCookie(response, refreshToken.getToken());

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseLoginDTO(accessToken));

	}
	
	@PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response) {
        
        refreshTokenService.logout(refreshToken);
        
        
       CreateCookie.deleteRefreshTokenCookie(response);
        
      
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada correctamente"));
    }

	@PostMapping("/refreshtoken")
    public ResponseEntity<ResponseLoginDTO> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshToken, 
            HttpServletResponse response
    ) {
     
        Map<String, String> tokens = refreshTokenService.renovateAccessToken(refreshToken);

        String newAccess = tokens.get("access_token");
        String newRefresh = tokens.get("refresh_token");

        CreateCookie.setRefreshTokenCookie(response, newRefresh);

        return ResponseEntity.ok(new ResponseLoginDTO(newAccess));
    }
}

