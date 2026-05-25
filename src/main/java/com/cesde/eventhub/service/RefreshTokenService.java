package com.cesde.eventhub.service;

import java.time.Instant; 
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.RefreshToken;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.exception.custom.Unauthorized;
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
		        UserResponseDTO userToken = userMapper.toDTO(user);	 

		        refreshTokenRepository.deleteByUsuario(currentToken.getUser());
		        
		        RefreshToken newRefreshToken = createRefreshToken(userToken);

		        return newRefreshToken;
		    }
	        
	        
	        private RefreshToken validateRefreshToken(String refreshToken) {

	        	
	        	
		        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
		                .orElseThrow(() -> new Unauthorized("Token inválido"));

		        if (token.isExpired()) {
		            refreshTokenRepository.deleteByexpirationDate();;
		            throw new Unauthorized("Token expirado");
		        }

		        return token;
		    }


         @Transactional
	    public Map<String, String> renovateAccessToken(String refreshToken) {
	    	
	       RefreshToken validToken = validateRefreshToken(refreshToken);

	        User user = validToken.getUser();
	        
	        UserResponseDTO userDTO = userMapper.toDTO(user);
	        
	        RefreshToken newRefresh = rotateRefreshToken(validToken);
	      
	        
	String newAccess = jwtService.generateAccessToken(
	        						userDTO.getId(),
	                				userDTO.getEmail(),
	                				userDTO.getRoles()
	                				); 
	        
	       
	Map<String, String> tokens = new HashMap<>();
    tokens.put("access_token", newAccess);
    tokens.put("refresh_token", newRefresh.getToken());
    
    return tokens;
	    }
	    
	    
	}


