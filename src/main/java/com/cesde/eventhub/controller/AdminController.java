package com.cesde.eventhub.controller;

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

import com.cesde.eventhub.dto.PlaceDTO;
import com.cesde.eventhub.dto.request.UpdatePlaceDTO;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.service.PlaceService;

import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	
	private final PlaceService placeService;
 
	@PostMapping("/crearlugar")
	public ResponseEntity<?> createPlace(@Valid @RequestBody PlaceDTO place){
	
		try {
		return ResponseEntity.status(HttpStatus.OK).body(placeService.createPlace(place));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debe ingresar todos los campos");
		}
	}
	
	@GetMapping("/lugaresactivos")
	public ResponseEntity<Page<Place>> activesPlaces(Pageable pageable){
		return ResponseEntity.ok(placeService.activesPlaces(pageable));
	}
	
	@PutMapping("/actualizarlugar/{id}")
	public ResponseEntity<?> updatePlace(
	        @PathVariable Long id,
	        @RequestBody @Valid UpdatePlaceDTO placeDTO) {
		
		try {
			return ResponseEntity.status(HttpStatus.OK).body(placeService.updatePlace(id, placeDTO));
		} catch (RuntimeException e) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe un lugar con ese id");
	    }
		
	 }
	
	@DeleteMapping("eliminarlugar/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Long id){
		try {
			placeService.deletePlace(id);
			  return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eliminado correctamente");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe un lugar con ese id");
		}
  	
  			  
    }
	
	
}