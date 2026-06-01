package com.cesde.eventhub.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.cesde.eventhub.dto.request.ZoneRegisterDTO;
import com.cesde.eventhub.dto.response.PaginatedResponseDTO;
import com.cesde.eventhub.dto.response.ZoneResponseDTO;
import com.cesde.eventhub.service.ZoneService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/zonas")
public class ZoneController {
	
	private final ZoneService zoneService;
	
	
	@PostMapping("/lugar/{lugarId}/crear")
	public ResponseEntity<ZoneResponseDTO> createZone(@PathVariable Long lugarId,  @Valid @RequestBody ZoneRegisterDTO zoneRequest){
		
		ZoneResponseDTO zoneResponse = zoneService.createZone(zoneRequest, lugarId);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(zoneResponse);
	}
	
	@PutMapping("/lugar/{lugarId}/actualizar/{zonaId}")
	public ResponseEntity<ZoneResponseDTO> updateZone (
			@PathVariable Long lugarId,
			@PathVariable Long zonaId,
			@Valid @RequestBody ZoneRegisterDTO zoneUpdate){
		
		ZoneResponseDTO zoneResponse = zoneService.updateZone(lugarId,zonaId, zoneUpdate);
		
		return ResponseEntity.status(HttpStatus.OK).body(zoneResponse);
		
	}
	
	@GetMapping("/lugar/{lugarId}")
    public ResponseEntity<PaginatedResponseDTO<ZoneResponseDTO>> getByPlace(
            @PathVariable Long lugarId,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        
        return ResponseEntity.status(HttpStatus.OK).body(zoneService.getZonesByPlace(lugarId, pageable));
    }
	
	@DeleteMapping("/eliminar/{zoneId}")
	public ResponseEntity<Void> deleteZone(@PathVariable Long zoneId) {
	    zoneService.deleteZone(zoneId);
	    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
}
