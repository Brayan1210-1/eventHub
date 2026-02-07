package com.cesde.eventhub.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.UsuarioDTO;
import com.cesde.eventhub.servicio.UsuarioServicio;



@RestController
@RequestMapping("/api/v1/login")
public class UsuarioControlador {

	@Autowired
	private UsuarioServicio usuarioServicio;
	
	@PostMapping
	public ResponseEntity<?> crearUsuario(UsuarioDTO usuario) {

        try {
            UsuarioDTO usuarioCreado = usuarioServicio.crearCliente(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Datos inválidos o email ya existe");
        }
    }
	
}
