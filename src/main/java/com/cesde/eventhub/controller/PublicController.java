package com.cesde.eventhub.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.request.FilterEventsPublicsDTO;
import com.cesde.eventhub.dto.response.EventPublicDTO;
import org.springframework.data.domain.Sort;
import com.cesde.eventhub.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/publico/eventos")
@RequiredArgsConstructor
public class PublicController {

    private final EventService eventService;

    @GetMapping("/filtrar")
    public ResponseEntity<Page<EventPublicDTO>> listEvents(
            @ModelAttribute FilterEventsPublicsDTO filters,
            @PageableDefault(size = 12, sort = "eventDate", direction = Sort.Direction.ASC) Pageable pageable) {
        
        return ResponseEntity.ok(eventService.getPublicEvents(filters, pageable));
    }
}