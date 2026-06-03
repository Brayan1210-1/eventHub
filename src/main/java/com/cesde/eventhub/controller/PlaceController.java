package com.cesde.eventhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.PlaceDetailDTO;
import com.cesde.eventhub.dto.PlaceListDTO;
import com.cesde.eventhub.dto.response.PaginatedResponseDTO;
import com.cesde.eventhub.service.PlaceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lugares")
@RequiredArgsConstructor
public class PlaceController {

	private final PlaceService placeService;
	
	@GetMapping("/buscar")
    public ResponseEntity<PaginatedResponseDTO<PlaceListDTO>> getBasicPlaces(
            @RequestParam(required = false, defaultValue = "") String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(placeService.getBasicPlacesList(nombre, page, size));
    }
	
	@GetMapping("/{lugarId}")
	public ResponseEntity<PlaceDetailDTO> getPlaceDetail(@PathVariable Long lugarId){
		
		return ResponseEntity.ok(placeService.getPlaceDetails(lugarId));
	}
}
