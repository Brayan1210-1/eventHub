package com.cesde.eventhub.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.UsuarioDTO;
import com.cesde.eventhub.servicio.UsuarioServicio;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid; 


@RestController
@RequestMapping("/api/v1")
public class UsuarioControlador {

	@Autowired
	private UsuarioServicio usuarioServicio;
	
	@PostMapping("/registro")
	public ResponseEntity<UsuarioDTO> crearUsuario(@Valid @RequestBody UsuarioDTO usuario) {

        
            UsuarioDTO usuarioCreado = usuarioServicio.crearCliente(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
       
    }
	
	
}
