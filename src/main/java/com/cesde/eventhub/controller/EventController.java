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

import com.cesde.eventhub.dto.EventCancelDTO;
import com.cesde.eventhub.dto.request.EventRegisterDTO;
import com.cesde.eventhub.dto.response.EventResponseDTO;
import com.cesde.eventhub.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/crear")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRegisterDTO dto) {
        EventResponseDTO response = eventService.createEvent(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PatchMapping("/publicar/{eventId}")
    public ResponseEntity<String> publishEvent(@PathVariable Long eventId) {
        eventService.publishEvent(eventId);
        return ResponseEntity.ok("El evento ha sido publicado exitosamente.");
    }
    
    @GetMapping("/todos")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
    
    @PatchMapping("/cancelar/{eventId}")
    public ResponseEntity<String> cancelEvent(
            @PathVariable Long eventId, 
            @Valid @RequestBody EventCancelDTO dto) {
        
        eventService.cancelEvent(eventId, dto);
        
        return ResponseEntity.ok("El evento ha sido cancelado exitosamente " +
                "Las órdenes pagadas han sido marcadas para reembolso y las boletas anuladas");
    }
}