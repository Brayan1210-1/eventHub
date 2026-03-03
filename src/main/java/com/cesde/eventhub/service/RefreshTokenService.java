package com.cesde.eventhub.service;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cesde.eventhub.dto.response.ResponseLoginDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.RefreshToken;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.mapper.UserMapper;
import com.cesde.eventhub.repository.RefreshTokenRepository;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	
	    private final RefreshTokenRepository refreshTokenRepositorio;
	    
	    private final UserMapper mapper;

	    private final JwtService jwtServicio;
	   
	    private final UserService usuarioServ;

	    @Value("${jwt.refresh-expiration}")
		 private long jwtRefresh;
	    
	        public RefreshToken crearRefreshToken(UserResponseDTO usuario) {
	        	
	        	User usuarioEntity = usuarioServ.findByEmail(usuario.getEmail());

	            // Generar refresh token con JwtServicio
	            String refreshTokenJwt = jwtServicio.generarRefreshToken(usuario.getId());

	            RefreshToken refreshToken = new RefreshToken();
	            refreshToken.setUsuario(usuarioEntity);
	            refreshToken.setToken(refreshTokenJwt);
	            refreshToken.setFechaExpiracion(
	                Instant.now().plusMillis(jwtRefresh) 
	            );

	            return refreshTokenRepositorio.save(refreshToken);
	        }
	        
	        @Transactional
	        private RefreshToken rotarRefreshToken(RefreshToken tokenActual) {

		        User usuario = tokenActual.getUsuario();
		        UserResponseDTO usuarioToken = mapper.haciaDto(usuario);	 

		        refreshTokenRepositorio.deleteByUsuario(tokenActual.getUsuario());
		        
		        RefreshToken nuevoRefreshToken = crearRefreshToken(usuarioToken);

		        return nuevoRefreshToken;
		    }
	        
	        private RefreshToken validarRefreshToken(String refreshToken) {

		        RefreshToken token = refreshTokenRepositorio.findByToken(refreshToken)
		                .orElseThrow(() -> new RuntimeException("Token inválido"));

		        if (token.estaExpirado()) {
		            refreshTokenRepositorio.deleteByFechaExpiracion();
		            throw new RuntimeException("Token expirado");
		        }

		        return token;
		    }


         @Transactional
	    public ResponseLoginDTO renovarAccessToken(String refreshToken) {
	    	
	       RefreshToken tokenValido = validarRefreshToken(refreshToken);

	        User usuario = tokenValido.getUsuario();
	        
	        UserResponseDTO usuarioT = mapper.haciaDto(usuario);
	        
	        RefreshToken nuevoRefresh = rotarRefreshToken(tokenValido);
	      
	        //CAMBIAR AUTH CONTROLADOR
	String nuevoAccess = jwtServicio.generarAccessToken(
	        						usuarioT.getId(),
	                				usuarioT.getEmail(),
	                				usuarioT.getRol()
	                				); 
	        
	        return new ResponseLoginDTO(nuevoRefresh.getToken(), nuevoAccess);
	        
	    }
	    
	    
	}


