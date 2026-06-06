package com.cesde.eventhub.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.EventCancelDTO;
import com.cesde.eventhub.dto.MessageDTO;
import com.cesde.eventhub.dto.request.EventRegisterDTO;
import com.cesde.eventhub.dto.request.TicketValidationRequestDTO;
import com.cesde.eventhub.dto.response.EventReportResponseDTO;
import com.cesde.eventhub.dto.response.EventResponseDTO;
import com.cesde.eventhub.dto.response.OrderHistoryResponseDTO;
import com.cesde.eventhub.dto.response.PaginatedResponseDTO;
import com.cesde.eventhub.dto.response.TicketValidationResponseDTO;
import com.cesde.eventhub.enums.EventStatus;
import com.cesde.eventhub.enums.OrderStatus;
import com.cesde.eventhub.service.EventService;
import com.cesde.eventhub.service.OrderService;
import com.cesde.eventhub.service.TicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final TicketService ticketService;
    private final OrderService orderService;
    

    @PostMapping("/crear")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRegisterDTO dto) {
        EventResponseDTO response = eventService.createEvent(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PatchMapping("/publicar/{eventId}")
    public ResponseEntity<MessageDTO> publishEvent(@PathVariable Long eventId) {
        eventService.publishEvent(eventId);
        MessageDTO response = new MessageDTO("El evento ha sido publicado exitosamente.");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/mis-eventos")
    public ResponseEntity<PaginatedResponseDTO<EventResponseDTO>> getMyEvents(
            @RequestParam(required = false) EventStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        UUID userId = UUID.fromString(authentication.getName()); 
        
        return ResponseEntity.ok(eventService.getMyEvents(userId, status, page, size));
    }
    
    @GetMapping("/todos")
    public ResponseEntity<PaginatedResponseDTO<EventResponseDTO>> getAllEvents(
    		@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(eventService.getAllEvents(pageable));
    }
    
    @PatchMapping("/cancelar/{eventId}")
    public ResponseEntity<MessageDTO> cancelEvent(
            @PathVariable Long eventId, 
            @Valid @RequestBody EventCancelDTO dto) {
        
        eventService.cancelEvent(eventId, dto);
        
        MessageDTO response = new MessageDTO(
    "El evento ha sido cancelado exitosamente."
    + " Las órdenes pagadas han sido marcadas para reembolso y las boletas anuladas");
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{eventId}/boletas/validar")
    @PreAuthorize("hasRole('ORGANIZADOR')")
    public ResponseEntity<TicketValidationResponseDTO> scanAndValidateTicket(
    		    @PathVariable Long eventId,
            @Valid @RequestBody TicketValidationRequestDTO request) {
        
        return ResponseEntity.ok(ticketService.validateTicket(eventId,request));
    }
    
    @GetMapping("/mis-ventas")
    @PreAuthorize("hasRole('ORGANIZADOR')")
    public ResponseEntity<PaginatedResponseDTO<OrderHistoryResponseDTO>> getOrganizerSalesHistory(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate, 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(orderService.getOrganizerSalesHistory(eventId, status, purchaseDate, page, size));
    }
    
    @GetMapping("/{eventId}/reporte")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public ResponseEntity<EventReportResponseDTO> getEventReport(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventReport(eventId));
    }
}