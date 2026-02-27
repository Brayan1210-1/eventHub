package com.cesde.eventhub.servicio;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.modelos.RefreshToken;
import com.cesde.eventhub.modelos.Usuario;
import com.cesde.eventhub.repositorio.RefreshTokenRepositorio;
import com.cesde.eventhub.dto.RespuestaLoginDTO;
import com.cesde.eventhub.dto.UsuarioRespuestaDTO;
import com.cesde.eventhub.mapper.UsuarioMapper;


@Service
public class RefreshTokenServicio {
	
	    @Autowired
	    private RefreshTokenRepositorio refreshTokenRepositorio;
	    
	    @Autowired
	    private UsuarioMapper mapper;

	    @Autowired
	    private JwtServicio jwtServicio;
	    
	    @Autowired
	    private UsuarioServicio usuarioServ;

	    @Value("${jwt.refresh-expiration}")
		 private long jwtRefresh;
	    
	        public RefreshToken crearRefreshToken(UsuarioRespuestaDTO usuario) {
	        	
	        	Usuario usuarioEntity = usuarioServ.findByEmail(usuario.getEmail());

	            // Generar refresh token con JwtServicio
	            String refreshTokenJwt = jwtServicio.generarRefreshToken(usuario.getEmail());

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
	                				usuarioT.getRol(),
	                				usuarioT.getNombre()); 
	        
	        return new RespuestaLoginDTO(nuevoRefresh.getToken(), nuevoAccess);
	        
	    }
	    
	    
	   
	    
	   
	}


