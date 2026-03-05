package com.cesde.eventhub.controller;

import org.springframework.data.domain.Page;
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
import com.cesde.eventhub.dto.response.ZoneResponseDTO;
import com.cesde.eventhub.service.ZoneService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/zonas")
public class ZoneController {
	
	private final ZoneService zoneService;
	
	
	@PostMapping("/crear")
	public ResponseEntity<ZoneResponseDTO> createZone(@Valid @RequestBody ZoneRegisterDTO zoneRequest){
		
		ZoneResponseDTO zoneResponse = zoneService.createZone(zoneRequest);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(zoneResponse);
	}
	
	@PutMapping("/actualizar/{id}")
	public ResponseEntity<ZoneResponseDTO> updateZone (@PathVariable Long id,@Valid @RequestBody ZoneRegisterDTO zoneUpdate){
		
		ZoneResponseDTO zoneResponse = zoneService.updateZone(id, zoneUpdate);
		
		return ResponseEntity.status(HttpStatus.OK).body(zoneResponse);
		
	}
	
	@GetMapping("/lugar/{id}")
    public ResponseEntity<Page<ZoneResponseDTO>> getByPlace(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        
        return ResponseEntity.status(HttpStatus.OK).body(zoneService.getZonesByPlace(id, pageable));
    }
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
	    zoneService.deleteZone(id);
	    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
}
