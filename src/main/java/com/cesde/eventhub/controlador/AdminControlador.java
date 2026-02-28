package com.cesde.eventhub.controlador;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.ActualizarLugarDTO;
import com.cesde.eventhub.dto.LugarDTO;
import com.cesde.eventhub.modelos.Lugar;
import com.cesde.eventhub.servicio.LugarServicio;

import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminControlador {
	
	@Autowired
	private LugarServicio lugarServicio;
 
	@PostMapping("/crearlugar")
	public ResponseEntity<?> crearLugar(@Valid @RequestBody LugarDTO lugar){
	

		return ResponseEntity.status(HttpStatus.OK).body(lugarServicio.crearLugar(lugar));
		
	}
	
	@GetMapping("/lugaresactivos")
	public ResponseEntity<Page<Lugar>> lugaresActivos(Pageable pageable){
		return ResponseEntity.ok(lugarServicio.lugaresActivos(pageable));
	}
	
	@PutMapping("/actualizarlugar/{id}")
	public ResponseEntity<?> actualizarLugar(
	        @PathVariable Long id,
	        @RequestBody @Valid ActualizarLugarDTO lugarDTO) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(lugarServicio.actualizarLugar(id, lugarDTO));
		} catch (RuntimeException e) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe un lugar con ese id");
	    }
	 }
	
	@DeleteMapping("eliminarlugar/{id}")
    public ResponseEntity<?> eliminarLugar(@PathVariable Long id){
		try {
			lugarServicio.eliminarLugar(id);
			  return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eliminado correctamente");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe un lugar con ese id");
		}
  	
  			  
    }
	
	
}