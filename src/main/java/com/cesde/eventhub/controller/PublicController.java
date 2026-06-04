package com.cesde.eventhub.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.request.FilterEventsPublicsDTO;
import com.cesde.eventhub.dto.response.EventDetailPublicDTO;
import com.cesde.eventhub.dto.response.EventPublicDTO;
import com.cesde.eventhub.dto.response.PaginatedResponseDTO;

import org.springframework.data.domain.Sort;
import com.cesde.eventhub.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/publico/eventos")
@RequiredArgsConstructor
public class PublicController {

    private final EventService eventService;

    @GetMapping("/filtrar")
    public ResponseEntity<PaginatedResponseDTO<EventPublicDTO>> listEvents(
            @ModelAttribute FilterEventsPublicsDTO filters,
            @PageableDefault(size = 10, page = 0, sort = "eventDate", direction = Sort.Direction.ASC) Pageable pageable) {
        
        return ResponseEntity.ok(eventService.getPublicEvents(filters, pageable));
    }
    
    @GetMapping("/detalle/{id}")
    public ResponseEntity<EventDetailPublicDTO> getEventDetail(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventDetail(id));
    }
}