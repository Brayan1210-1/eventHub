package com.cesde.eventhub.controller;


import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.MessageDTO;
import com.cesde.eventhub.dto.PlaceDTO;
import com.cesde.eventhub.dto.request.AdminUserRegisterDTO;
import com.cesde.eventhub.dto.request.UpdatePlaceDTO;
import com.cesde.eventhub.dto.response.GeneralReportResponseDTO;
import com.cesde.eventhub.dto.response.PaginatedResponseDTO;
import com.cesde.eventhub.dto.response.PlaceResponseDTO;
import com.cesde.eventhub.service.OrderService;
import com.cesde.eventhub.service.PlaceService;
import com.cesde.eventhub.service.UserService;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	private final UserService userService;
	private final PlaceService placeService;
	private final OrderService orderService;
 
	@PostMapping("/crearlugar")
	public ResponseEntity<PlaceDTO> createPlace(@Valid @RequestBody PlaceDTO place){
	
		return ResponseEntity.status(HttpStatus.OK).body(placeService.createPlace(place));
	}
	
	@GetMapping("/lugaresactivos")
	public ResponseEntity<PaginatedResponseDTO<PlaceResponseDTO>> activesPlaces( 
			@PageableDefault(size = 10, page = 0) Pageable pageable){
		return ResponseEntity.ok(placeService.activesPlaces(pageable));
	}
	
	@PatchMapping("/actualizarlugar/{id}")
	public ResponseEntity<UpdatePlaceDTO> updatePlace(
	        @PathVariable Long id,
	        @RequestBody @Valid UpdatePlaceDTO placeDTO) {
		
			return ResponseEntity.status(HttpStatus.OK).body(placeService.updatePlace(id, placeDTO));
	 }
	
	@DeleteMapping("eliminar/{id}")
    public ResponseEntity<MessageDTO> deletePlace(@PathVariable Long id){
		
			placeService.deletePlace(id);
			MessageDTO message = new MessageDTO("Lugar eliminado correctamente");
			  return ResponseEntity.status(HttpStatus.NO_CONTENT).body(message);
    }
	
	
    @PostMapping("/crear/usuarios")
    public ResponseEntity<MessageDTO> createUser(@Valid @RequestBody AdminUserRegisterDTO request) {
         MessageDTO response = userService.createUserByAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/reporte-general")
    public ResponseEntity<GeneralReportResponseDTO> getGeneralReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        return ResponseEntity.ok(orderService.getGeneralReport(fechaInicio, fechaFin));
    }
	
	
}