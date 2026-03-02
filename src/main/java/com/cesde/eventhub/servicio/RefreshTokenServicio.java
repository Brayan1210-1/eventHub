package com.cesde.eventhub.servicio;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.modelos.RefreshToken;
import com.cesde.eventhub.modelos.Usuario;
import com.cesde.eventhub.repositorio.RefreshTokenRepositorio;

import lombok.RequiredArgsConstructor;

import com.cesde.eventhub.dto.response.RespuestaLoginDTO;
import com.cesde.eventhub.dto.response.UsuarioRespuestaDTO;
import com.cesde.eventhub.mapper.UsuarioMapper;


@Service
@RequiredArgsConstructor
public class RefreshTokenServicio {
	
	    private final RefreshTokenRepositorio refreshTokenRepositorio;
	    
	    private final UsuarioMapper mapper;

	    private final JwtServicio jwtServicio;
	   
	    private final UsuarioServicio usuarioServ;

	    @Value("${jwt.refresh-expiration}")
		 private long jwtRefresh;
	    
	        public RefreshToken crearRefreshToken(UsuarioRespuestaDTO usuario) {
	        	
	        	Usuario usuarioEntity = usuarioServ.findByEmail(usuario.getEmail());

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

		        Usuario usuario = tokenActual.getUsuario();
		        UsuarioRespuestaDTO usuarioToken = mapper.haciaDto(usuario);	 

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
	    public RespuestaLoginDTO renovarAccessToken(String refreshToken) {
	    	
	       RefreshToken tokenValido = validarRefreshToken(refreshToken);

	        Usuario usuario = tokenValido.getUsuario();
	        
	        UsuarioRespuestaDTO usuarioT = mapper.haciaDto(usuario);
	        
	        RefreshToken nuevoRefresh = rotarRefreshToken(tokenValido);
	      
	        //CAMBIAR AUTH CONTROLADOR
	String nuevoAccess = jwtServicio.generarAccessToken(
	        						usuarioT.getId(),
	                				usuarioT.getEmail(),
	                				usuarioT.getRol()
	                				); 
	        
	        return new RespuestaLoginDTO(nuevoRefresh.getToken(), nuevoAccess);
	        
	    }
	    
	    
	}


