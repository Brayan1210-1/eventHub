package com.cesde.eventhub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.request.TicketPriceRegisterDTO;
import com.cesde.eventhub.dto.response.TicketPriceResponseDTO;
import com.cesde.eventhub.service.TicketPriceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets-precios")
@RequiredArgsConstructor
public class TicketPriceController {

    private final TicketPriceService ticketPriceService;

    @PostMapping("/crear")
    public ResponseEntity<TicketPriceResponseDTO> create(@Valid @RequestBody TicketPriceRegisterDTO dto) {
        return new ResponseEntity<>(ticketPriceService.createTicketPrice(dto), HttpStatus.CREATED);
    }
    
    @GetMapping("/evento/{zoneId}")
    public ResponseEntity<List<TicketPriceResponseDTO>> getPricesByEvent(@PathVariable Long zoneId) {
        List<TicketPriceResponseDTO> prices = ticketPriceService.getPricesByEvent(zoneId);
        return ResponseEntity.ok(prices);
    }
}