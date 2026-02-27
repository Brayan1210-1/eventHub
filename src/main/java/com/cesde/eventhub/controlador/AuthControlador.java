package com.cesde.eventhub.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.LoginDTO;
import com.cesde.eventhub.dto.PeticionTokenRefresh;
import com.cesde.eventhub.dto.RespuestaLoginDTO;
import com.cesde.eventhub.dto.UsuarioRegistroDTO;
import com.cesde.eventhub.dto.UsuarioRespuestaDTO;
import com.cesde.eventhub.modelos.RefreshToken;
import com.cesde.eventhub.servicio.JwtServicio;
import com.cesde.eventhub.servicio.RefreshTokenServicio;
import com.cesde.eventhub.servicio.UsuarioServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/autenticacion")
public class AuthControlador {
	
	@Autowired
	private UsuarioServicio usuarioServicio;
	
	@Autowired
	private JwtServicio jwtServicio;
	
	@Autowired
	private RefreshTokenServicio refreshTokenServicio;
	
	
	@PostMapping("/registro")
	public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioRegistroDTO usuario) {

          try {
            UsuarioRespuestaDTO usuarioCreado = usuarioServicio.crearCliente(usuario);
            
            String token = jwtServicio.generarAccessToken(
                    usuarioCreado.getId(), 
                    usuarioCreado.getEmail(), 
                    usuarioCreado.getRol(),
                    usuarioCreado.getNombre()
                );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
            } catch (RuntimeException runtimeException) {
            	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un usuario con ese email, documento o teléfono");
            }
    }
	
	@PostMapping("/login")
	public ResponseEntity<?> iniciarSesion(@Valid @RequestBody LoginDTO login){
		
		try {
		UsuarioRespuestaDTO usuario = usuarioServicio.iniciarSesion(login);
		
		String accessToken = jwtServicio.generarAccessToken(
				usuario.getId(), 
				usuario.getEmail(), 
				usuario.getRol(), 
				usuario.getNombre()
				);
		
		RefreshToken refreshToken = refreshTokenServicio.crearRefreshToken(usuario);
		
		//Añadir clase para manejo de excepciones
       return ResponseEntity.status(HttpStatus.OK).body(new RespuestaLoginDTO(refreshToken.getToken(),accessToken));	
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe un usuario con ese email o la contraseña es incorrecta");
		} 
		
	}
	
	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refrescarToken(@Valid @RequestBody PeticionTokenRefresh token) {
	    try {
	        RespuestaLoginDTO nuevosTokens = refreshTokenServicio.renovarAccessToken(token.getTokenRefresh());
	 
	        return ResponseEntity.status(HttpStatus.OK).body(nuevosTokens);
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	    }
	}

}
