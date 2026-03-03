package com.cesde.eventhub.service;

import java.time.Instant;
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
	
	    private final RefreshTokenRepository refreshTokenRepository;
	    
	    private final UserMapper userMapper;

	    private final JwtService jwtService;
	   
	    private final UserService userService;

	    @Value("${jwt.refresh-expiration}")
		 private long jwtRefresh;
	    
	        public RefreshToken createRefreshToken(UserResponseDTO user) {
	        	
	        	User userEntity = userService.findByEmail(user.getEmail());

	            // Generar refresh token con JwtServicio
	            String refreshTokenJwt = jwtService.generateRefreshToken(user.getId());

	            RefreshToken refreshToken = new RefreshToken();
	            refreshToken.setUser(userEntity);
	            refreshToken.setToken(refreshTokenJwt);
	            refreshToken.setExpirationDate(
	                Instant.now().plusMillis(jwtRefresh) 
	            );

	            return refreshTokenRepository.save(refreshToken);
	        }
	        
	        @Transactional
	        private RefreshToken rotateRefreshToken(RefreshToken currentToken) {

		        User user = currentToken.getUser();
		        UserResponseDTO userToken = userMapper.haciaDto(user);	 

		        refreshTokenRepository.deleteByUsuario(currentToken.getUser());
		        
		        RefreshToken nuevoRefreshToken = createRefreshToken(userToken);

		        return nuevoRefreshToken;
		    }
	        
	        private RefreshToken validateRefreshToken(String refreshToken) {

		        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
		                .orElseThrow(() -> new RuntimeException("Token inválido"));

		        if (token.isExpired()) {
		            refreshTokenRepository.deleteByexpirationDate();;
		            throw new RuntimeException("Token expirado");
		        }

		        return token;
		    }


         @Transactional
	    public ResponseLoginDTO renovateAccessToken(String refreshToken) {
	    	
	       RefreshToken validToken = validateRefreshToken(refreshToken);

	        User user = validToken.getUser();
	        
	        UserResponseDTO userDTO = userMapper.haciaDto(user);
	        
	        RefreshToken newRefresh = rotateRefreshToken(validToken);
	      
	        //CAMBIAR AUTH CONTROLADOR
	String newAccess = jwtService.generateAccessToken(
	        						userDTO.getId(),
	                				userDTO.getEmail(),
	                				userDTO.getRoles()
	                				); 
	        
	        return new ResponseLoginDTO(newRefresh.getToken(), newAccess);
	        
	    }
	    
	    
	}


