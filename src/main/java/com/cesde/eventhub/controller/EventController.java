package com.cesde.eventhub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.request.EventRegisterDTO;
import com.cesde.eventhub.dto.response.EventResponseDTO;
import com.cesde.eventhub.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/crear")
    @Operation(summary = "Create a new event", description = "Registers a new event in a specific zone. Only for ADMIN or ORGANIZADOR.")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRegisterDTO dto) {
        EventResponseDTO response = eventService.createEvent(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{eventId}/publicar")
    public ResponseEntity<String> publishEvent(@PathVariable Long eventId) {
        eventService.publishEvent(eventId);
        return ResponseEntity.ok("El evento ha sido publicado exitosamente.");
    }
    
    @GetMapping("/todos")
    @Operation(summary = "Get all events", description = "Retrieves a list of all registered events.")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
}