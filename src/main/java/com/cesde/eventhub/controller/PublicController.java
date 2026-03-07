package com.cesde.eventhub.controller;
/**
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.response.EventPublicDTO;
import com.cesde.eventhub.enums.Category;
import com.cesde.eventhub.service.ClientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/publico")
@RequiredArgsConstructor
public class PublicController {
	
	private final ClientService clientService;
	 
	 USER STORY 010 IN PROCESS
	 
	@GetMapping("/eventos")
    public ResponseEntity<Page<EventPublicDTO>> getPublicEvents(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false, name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false, name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        
        Page<EventPublicDTO> events = clientService.getPublicEvents(category, city, startDate, endDate, pageable);
        return ResponseEntity.ok(events);
    }
 
	
	
}
**/