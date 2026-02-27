package com.cesde.eventhub.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.CrearLugarDTO;
import com.cesde.eventhub.servicio.LugarServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminControlador {
	
	@Autowired
	private LugarServicio lugarServicio;
 
	@PostMapping("/crearlugar")
	public ResponseEntity<?> crearLugar(@Valid @RequestBody CrearLugarDTO lugar){
	

		return ResponseEntity.status(HttpStatus.OK).body(lugarServicio.crearLugar(lugar));
		
	}
}
